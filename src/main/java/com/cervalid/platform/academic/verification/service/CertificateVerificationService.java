package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.timeline.service.PublicTimelineQueryService;
import com.cervalid.platform.academic.verification.dto.request.VerifyCertificateRequest;
import com.cervalid.platform.academic.verification.dto.response.VerifyCertificateResponse;
import com.cervalid.platform.academic.verification.dto.view.PublicCertificateView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import com.cervalid.platform.academic.verification.enums.VerificationSource;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import com.cervalid.platform.academic.verification.mapper.VerificationResponseMapper;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.shared.hashing.HashService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateVerificationService {

    private final CertificateRepository certificateRepository;
    private final VerificationRecordRepository verificationRecordRepository;
    private final VerificationResponseMapper verificationResponseMapper;
    private final VerificationQueryService verificationQueryService;
    private final PublicTimelineQueryService publicTimelineQueryService;
    private final HashService hashService;
    private final PublicIdGenerator publicIdGenerator;

    @Transactional
    public VerifyCertificateResponse verify(
            VerifyCertificateRequest request,
            String ipAddress) {

        String certificateNumber =
                request.getCertificateNumber();

        String providedHash =
                hashService.normalizeHash(
                        request.getHash());

        Certificate certificate =
                certificateRepository
                        .findByCertificateNumber(
                                certificateNumber)
                        .orElse(null);

        //CERTIFICATE NOT FOUND
        if (certificate == null) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .certificateNumber(certificateNumber)
                            .providedHash(providedHash)
                            .valid(false)
                            .status(VerificationStatus.NOT_FOUND)
                            .verificationReason(
                                    "No existe un certificado con ese número.")
                            .verificationSource(
                                    VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            return verificationResponseMapper.toResponse(
                    null,
                    record,
                    null,
                    List.of(),
                    "No se encontró un certificado con la información proporcionada.",
                    VerificationLevel.LOCAL
            );
        }

        // public timeline
        List<PublicTimelineEventView> timeline =
                publicTimelineQueryService.getPublicTimeline(
                        certificate.getInstitutionId(),
                        certificate.getStudentId()
                );

        // certificado revocado
        if (certificate.getStatus() ==
                CertificateStatus.REVOKED) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .certificateId(certificate.getId())
                            .certificateNumber(certificate.getCertificateNumber())
                            .institutionId(certificate.getInstitutionId())
                            .providedHash(providedHash)
                            .storedHash(certificate.getVerificationHash())
                            .valid(false)
                            .status(VerificationStatus.REVOKED)
                            .verificationReason(
                                    "El certificado fue revocado por la institución.")
                            .verificationSource(VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            PublicCertificateView view =
                    verificationQueryService
                            .buildVerificationView(
                                    certificate.getStudentId(),
                                    certificate.getInstitutionId()
                            );

            return verificationResponseMapper.toResponse(
                    certificate,
                    record,
                    view,
                    timeline,
                    "Este certificado ha sido revocado por la institución emisora y ya no es válido.",
                    VerificationLevel.LOCAL
            );
        }

        //NORMAL HASH VALIDATION
        String storedHash =
                hashService.normalizeHash(
                        certificate.getVerificationHash());

        boolean valid =
                storedHash.equals(providedHash);

        VerificationStatus status =
                valid
                        ? VerificationStatus.VALID
                        : VerificationStatus.INVALID;

        VerificationRecord record =
                VerificationRecord.builder()
                        .publicId(publicIdGenerator.generate())
                        .certificateId(certificate.getId())
                        .certificateNumber(certificate.getCertificateNumber())
                        .institutionId(certificate.getInstitutionId())
                        .providedHash(providedHash)
                        .storedHash(storedHash)
                        .status(status)
                        .valid(valid)
                        .verificationReason(valid
                                ? "La integridad del certificado fue validada correctamente."
                                : "El hash proporcionado no coincide con el registrado para este certificado.")
                        .verificationSource(VerificationSource.PUBLIC_API)
                        .verifiedByIp(ipAddress)
                        .verifiedAt(LocalDateTime.now())
                        .build();

        verificationRecordRepository.save(record);

        PublicCertificateView view =
                verificationQueryService
                        .buildVerificationView(
                                certificate.getStudentId(),
                                certificate.getInstitutionId()
                        );

        return verificationResponseMapper.toResponse(
                certificate,
                record,
                view,
                timeline,
                valid
                        ? "La autenticidad del certificado fue verificada correctamente."
                        : "El código de verificación no coincide con el certificado.",
                valid
                        ? VerificationLevel.HASH_VALIDATED
                        : VerificationLevel.LOCAL
        );
    }

    // Verificación pública mediante el publicId del certificado
    @Transactional
    public VerifyCertificateResponse verifyPublic(
            UUID certificatePublicId,
            String ipAddress) {

        Certificate certificate =
                certificateRepository
                        .findByPublicId(certificatePublicId)
                        .orElse(null);

        // CERTIFICATE NOT FOUND
        if (certificate == null) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .valid(false)
                            .status(VerificationStatus.NOT_FOUND)
                            .verificationReason(
                                    "No existe un certificado con el publicId proporcionado.")
                            .verificationSource(
                                    VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            return verificationResponseMapper.toResponse(
                    null,
                    record,
                    null,
                    List.of(),
                    "No se encontró el certificado solicitado.",
                    VerificationLevel.LOCAL
            );
        }

        // public timeline
        List<PublicTimelineEventView> timeline =
                publicTimelineQueryService.getPublicTimeline(
                        certificate.getInstitutionId(),
                        certificate.getStudentId()
                );

        // CERTIFICATE REVOKED
        if (certificate.getStatus() ==
                CertificateStatus.REVOKED) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .certificateId(certificate.getId())
                            .certificateNumber(certificate.getCertificateNumber())
                            .institutionId(certificate.getInstitutionId())
                            .storedHash(certificate.getVerificationHash())
                            .valid(false)
                            .status(VerificationStatus.REVOKED)
                            .verificationReason(
                                    "El certificado fue revocado por la institución.")
                            .verificationSource(
                                    VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            PublicCertificateView view =
                    verificationQueryService
                            .buildVerificationView(
                                    certificate.getStudentId(),
                                    certificate.getInstitutionId()
                            );

            return verificationResponseMapper.toResponse(
                    certificate,
                    record,
                    view,
                    timeline,
                    "Este certificado ha sido revocado por la institución emisora y ya no es válido.",
                    VerificationLevel.LOCAL
            );
        }

        // publico existente + estado valido
        VerificationRecord record =
                VerificationRecord.builder()
                        .publicId(publicIdGenerator.generate())
                        .certificateId(certificate.getId())
                        .certificateNumber(certificate.getCertificateNumber())
                        .institutionId(certificate.getInstitutionId())

                        //.storedHash(certificate.getVerificationHash())
                        // No hay hash proporcionado.
                        // Por lo tanto, no existe validación hash.
                        .providedHash(null)
                        .storedHash(null)

                        .valid(true)
                        .status(VerificationStatus.VALID)
                        .verificationReason(
                                "El certificado existe y se encuentra vigente en CERVALID.")
                        .verificationSource(VerificationSource.PUBLIC_API)
                        .verifiedByIp(ipAddress)
                        .verifiedAt(LocalDateTime.now())
                        .build();

        verificationRecordRepository.save(record);

        PublicCertificateView view =
                verificationQueryService
                        .buildVerificationView(
                                certificate.getStudentId(),
                                certificate.getInstitutionId()
                        );

        return verificationResponseMapper.toResponse(
                certificate,
                record,
                view,
                timeline,
                "El certificado fue encontrado y se encuentra vigente.",
                VerificationLevel.LOCAL
        );
    }

    @Transactional(readOnly = true)
    public List<PublicTimelineEventView> getPublicTimeline(
            UUID certificatePublicId) {

        Certificate certificate =
                certificateRepository
                        .findByPublicId(certificatePublicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el certificado."
                                )
                        );

        return publicTimelineQueryService.getPublicTimeline(
                certificate.getInstitutionId(),
                certificate.getStudentId()
        );
    }
}