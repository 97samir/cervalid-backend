package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.credential.service.CredentialService;
import com.cervalid.platform.academic.credential.dto.response.CredentialVerificationResponse;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.timeline.service.PublicTimelineQueryService;
import com.cervalid.platform.academic.verification.dto.request.VerifyCredentialRequest;
import com.cervalid.platform.academic.verification.dto.response.CredentialVerificationResultResponse;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import com.cervalid.platform.academic.verification.enums.VerificationSource;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.shared.hashing.HashService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.cervalid.platform.academic.verification.proof.VerificationProofService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialVerificationService {

    private final CredentialRepository credentialRepository;
    private final VerificationRecordRepository verificationRecordRepository;
    private final CredentialService credentialService;
    private final VerificationProofService verificationProofService;
    private final HashService hashService;
    private final PublicIdGenerator publicIdGenerator;
    private final PublicTimelineQueryService publicTimelineQueryService;

    @Transactional
    public CredentialVerificationResultResponse verify(
            VerifyCredentialRequest request,
            String ipAddress
    ) {

        String credentialNumber =
                request.getCredentialNumber().trim();

        String providedHash =
                hashService.normalizeHash(
                        request.getVerificationHash()
                );

        // BUSCAR CREDENTIAL
        Credential credential =
                credentialRepository
                        .findByCredentialNumberAndDeletedFalse(
                                credentialNumber
                        )
                        .orElse(null);

        // CREDENTIAL NO EXISTE
        if (credential == null) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .credentialNumber(credentialNumber)
                            .providedHash(providedHash)
                            .valid(false)
                            .status(VerificationStatus.NOT_FOUND)
                            .verificationReason("No existe un credential con ese número.")
                            .verificationSource(VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            return CredentialVerificationResultResponse.builder()
                    .status(VerificationStatus.NOT_FOUND)
                    .verificationLevel(VerificationLevel.LOCAL)
                    .credential(null)
                    .build();
        }

        // CREDENTIAL REVOCADO
        String storedHash =
                hashService.normalizeHash(
                        credential.getVerificationHash()
                );

        boolean hashMatches =
                storedHash != null
                        && storedHash.equals(providedHash);

        // VALIDAR PRUEBA INTERNA
        boolean proofIsValid = false;

        if (credential.getCredentialHash() != null
                && !credential.getCredentialHash().isBlank()
                && storedHash != null) {

            String expectedVerificationHash =
                    hashService.normalizeHash(
                            verificationProofService
                                    .generateCredentialProof(
                                            credential.getCredentialHash()
                                    )
                    );

            proofIsValid =
                    expectedVerificationHash.equals(
                            storedHash
                    );
        }

        // HASH INVALIDO
        if (!hashMatches || !proofIsValid) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .credentialId(credential.getId())
                            .credentialNumber(credential.getCredentialNumber())
                            .institutionId(credential.getInstitutionId())
                            .providedHash(providedHash)
                            .storedHash(storedHash)
                            .valid(false)
                            .status(VerificationStatus.INVALID)
                            .verificationReason("El hash proporcionado no coincide con la prueba de verificación registrada.")
                            .verificationSource(VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            return CredentialVerificationResultResponse.builder()
                    .status(VerificationStatus.INVALID)
                    .verificationLevel(VerificationLevel.LOCAL)
                    .credential(null)
                    .build();
        }

        // CREDENTIAL REVOCADO
        if (credential.getStatus()
                == CredentialStatus.REVOKED) {

            VerificationRecord record =
                    VerificationRecord.builder()
                            .publicId(publicIdGenerator.generate())
                            .credentialId(credential.getId())
                            .credentialNumber(credential.getCredentialNumber())
                            .institutionId(credential.getInstitutionId())
                            .providedHash(providedHash)
                            .storedHash(storedHash)
                            .valid(false)
                            .status(VerificationStatus.REVOKED)
                            .verificationReason("El credential fue revocado por la institución emisora.")
                            .verificationSource(VerificationSource.PUBLIC_API)
                            .verifiedByIp(ipAddress)
                            .verifiedAt(LocalDateTime.now())
                            .build();

            verificationRecordRepository.save(record);

            CredentialVerificationResponse credentialResponse =
                    credentialService.getVerificationByPublicId(
                            credential.getPublicId()
                    );

            return CredentialVerificationResultResponse.builder()
                    .status(VerificationStatus.REVOKED)
                    .verificationLevel(VerificationLevel.HASH_VALIDATED)
                    .credential(credentialResponse)
                    .build();
        }

        // CREDENTIAL VALIDO
        VerificationRecord record =
                VerificationRecord.builder()
                        .publicId(publicIdGenerator.generate())
                        .credentialId(credential.getId())
                        .credentialNumber(credential.getCredentialNumber())
                        .institutionId(credential.getInstitutionId())
                        .providedHash(providedHash)
                        .storedHash(storedHash)
                        .valid(true)
                        .status(VerificationStatus.VALID)
                        .verificationReason("La autenticidad e integridad del credential fueron validadas correctamente.")
                        .verificationSource(VerificationSource.PUBLIC_API)
                        .verifiedByIp(ipAddress)
                        .verifiedAt(LocalDateTime.now())
                        .build();

        verificationRecordRepository.save(record);

        CredentialVerificationResponse credentialResponse =
                credentialService.getVerificationByPublicId(
                        credential.getPublicId()
                );

        return CredentialVerificationResultResponse.builder()
                .status(VerificationStatus.VALID)
                .verificationLevel(VerificationLevel.HASH_VALIDATED)
                .credential(credentialResponse)
                .build();
    }

    @Transactional(readOnly = true)
    public List<PublicTimelineEventView> getPublicTimeline(
            UUID credentialPublicId
    ) {

        Credential credential =
                credentialRepository
                        .findByPublicIdAndDeletedFalse(
                                credentialPublicId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "No se encontró el credential."
                                )
                        );

        return publicTimelineQueryService.getPublicTimeline(
                credential.getInstitutionId(),
                credential.getStudentId()
        );
    }
}