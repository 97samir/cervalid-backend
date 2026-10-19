package com.cervalid.platform.demo.data.verification;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.academic.verification.enums.VerificationSource;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import com.cervalid.platform.academic.verification.repository.VerificationRecordRepository;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoVerificationSeeder {

    private static final int VERIFICATIONS_PER_RESOURCE = 7;

    private final InstitutionRepository institutionRepository;
    private final CertificateRepository certificateRepository;
    private final CredentialRepository credentialRepository;
    private final VerificationRecordRepository verificationRepository;
    private final PublicIdGenerator publicIdGenerator;

    @Transactional
    public void seed() {
        seedInstitution(DemoInstitutionSeeder.UNIVERSITY_RUC);
        seedInstitution(DemoInstitutionSeeder.INSTITUTE_RUC);

        log.info("Demo verification seeding completed.");
    }

    private void seedInstitution(String institutionRuc) {
        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElse(null);

        if (institution == null) {
            log.warn("Skipping demo verifications: institution {} not found.",
                    institutionRuc);
            return;
        }

        Long institutionId = institution.getId();

        List<Certificate> certificates =
                certificateRepository
                        .findAllByInstitutionIdAndDeletedFalse(institutionId);

        List<Credential> credentials =
                credentialRepository
                        .findAllByInstitutionIdAndDeletedFalse(institutionId);

        for (Certificate certificate : certificates) {
            if (verificationRepository.countByCertificateId(
                    certificate.getId()) == 0) {
                seedCertificateHistory(certificate);
            }
        }

        for (Credential credential : credentials) {
            if (verificationRepository.countByCredentialId(
                    credential.getId()) == 0) {
                seedCredentialHistory(credential);
            }
        }

        // Registros de consulta sin un recurso encontrado.
        if (countNotFound(institutionId) == 0) {
            seedNotFoundHistory(institutionId);
        }
    }

    private void seedCertificateHistory(Certificate certificate) {
        boolean revoked = certificate.getStatus()
                == CertificateStatus.REVOKED;

        for (int i = 0; i < VERIFICATIONS_PER_RESOURCE; i++) {
            VerificationStatus status = revoked
                    ? VerificationStatus.REVOKED
                    : (i < 5
                    ? VerificationStatus.VALID
                    : VerificationStatus.INVALID);

            String storedHash = certificate.getVerificationHash();

            String providedHash = status == VerificationStatus.INVALID
                    ? "DEMO-INVALID-" + i
                    : storedHash;

            saveRecord(
                    certificate.getInstitutionId(),
                    certificate.getId(),
                    null,
                    certificate.getCertificateNumber(),
                    null,
                    providedHash,
                    storedHash,
                    status,
                    i
            );
        }
    }

    private void seedCredentialHistory(Credential credential) {
        boolean revoked = credential.getStatus()
                == CredentialStatus.REVOKED;

        for (int i = 0; i < VERIFICATIONS_PER_RESOURCE; i++) {
            VerificationStatus status = revoked
                    ? VerificationStatus.REVOKED
                    : (i < 5
                    ? VerificationStatus.VALID
                    : VerificationStatus.INVALID);

            String storedHash = credential.getVerificationHash();

            String providedHash = status == VerificationStatus.INVALID
                    ? "DEMO-INVALID-" + i
                    : storedHash;

            saveRecord(
                    credential.getInstitutionId(),
                    null,
                    credential.getId(),
                    null,
                    credential.getCredentialNumber(),
                    providedHash,
                    storedHash,
                    status,
                    i
            );
        }
    }

    private void seedNotFoundHistory(Long institutionId) {
        for (int i = 0; i < 4; i++) {
            saveRecord(
                    institutionId,
                    null,
                    null,
                    "DEMO-CERT-NOT-FOUND-" + i,
                    null,
                    "DEMO-UNKNOWN-HASH-" + i,
                    null,
                    VerificationStatus.NOT_FOUND,
                    i + 2
            );

            saveRecord(
                    institutionId,
                    null,
                    null,
                    null,
                    "DEMO-CRED-NOT-FOUND-" + i,
                    "DEMO-UNKNOWN-HASH-" + i,
                    null,
                    VerificationStatus.NOT_FOUND,
                    i + 4
            );
        }
    }

    private void saveRecord(
            Long institutionId,
            Long certificateId,
            Long credentialId,
            String certificateNumber,
            String credentialNumber,
            String providedHash,
            String storedHash,
            VerificationStatus status,
            int index
    ) {
        boolean valid = status == VerificationStatus.VALID;

        VerificationRecord record = VerificationRecord.builder()
                .publicId(publicIdGenerator.generate())
                .institutionId(institutionId)
                .certificateId(certificateId)
                .credentialId(credentialId)
                .certificateNumber(certificateNumber)
                .credentialNumber(credentialNumber)
                .providedHash(providedHash)
                .storedHash(storedHash)
                .status(status)
                .valid(valid)
                .verificationReason(resolveReason(status))
                .verificationSource(resolveSource(index))
                .verifiedByIp("192.0.2." + (10 + index))
                .verifiedAt(resolveVerifiedAt(index))
                .build();

        verificationRepository.save(record);
    }

    private String resolveReason(VerificationStatus status) {
        return switch (status) {
            case VALID ->
                    "Verificación demostrativa: la integridad del registro coincide.";
            case INVALID ->
                    "Verificación demostrativa: el hash proporcionado no coincide.";
            case REVOKED ->
                    "Verificación demostrativa: el recurso se encuentra revocado.";
            case NOT_FOUND ->
                    "Verificación demostrativa: no se encontró el recurso solicitado.";
        };
    }

    private VerificationSource resolveSource(int index) {
        VerificationSource[] sources = {
                VerificationSource.PUBLIC_API,
                VerificationSource.ADMIN_PANEL,
                VerificationSource.INSTITUTION_PORTAL,
                VerificationSource.BULK_PROCESS,
                VerificationSource.BLOCKCHAIN_VALIDATOR
        };

        return sources[Math.floorMod(index, sources.length)];
    }

    private LocalDateTime resolveVerifiedAt(int index) {
        return LocalDateTime.now()
                .minusDays(index % 30)
                .withHour(9 + (index % 9))
                .withMinute((index * 7) % 60)
                .withSecond(0)
                .withNano(0);
    }

    private long countNotFound(Long institutionId) {
        return verificationRepository
                .countByInstitutionIdAndStatus(
                        institutionId,
                        VerificationStatus.NOT_FOUND
                );
    }
}
