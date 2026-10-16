package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CredentialGenerationService {

    private final CredentialRepository credentialRepository;
    private final StudentRepository studentRepository;
    private final CredentialNumberGenerator credentialNumberGenerator;
    private final CredentialHashService credentialHashService;
    private final CredentialVerificationHashService credentialVerificationHashService;
    private final CredentialVerificationUrlBuilder credentialVerificationUrlBuilder;

    @Transactional
    public Credential generate(
            Long institutionId,
            Student student,
            Credential credential) {

        credential.setInstitutionId(institutionId);
        credential.setStudentId(student.getId());
        credential.setCredentialNumber(
                generateCredentialNumber(institutionId));
        credential.setStatus(CredentialStatus.DRAFT);
        credential.setDeleted(false);

        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential update(Credential credential) {
        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential issue(Credential credential) {

        credential.setStatus(CredentialStatus.ISSUED);
        credential.setIssuedAt(LocalDateTime.now());

        String credentialHash =
                credentialHashService.generateCredentialHash(
                        credential
                );

        credential.setCredentialHash(credentialHash);

        String verificationHash =
                credentialVerificationHashService
                        .generateVerificationHash(credentialHash);

        credential.setVerificationHash(verificationHash);

        String verificationUrl =
                credentialVerificationUrlBuilder.build(
                        credential.getPublicId()
                );

        credential.setVerificationUrl(verificationUrl);

        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential revoke(
            Credential credential,
            String reason) {

        credential.setStatus(CredentialStatus.REVOKED);
        credential.setRevokedAt(LocalDateTime.now());
        credential.setRevocationReason(reason);

        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential updateDocument(
            Credential credential,
            String documentHash,
            String documentUrl) {

        if (documentHash != null && !documentHash.isBlank()) {
            credential.setDocumentHash(documentHash.trim());
        }

        if (documentUrl != null && !documentUrl.isBlank()) {
            credential.setDocumentUrl(documentUrl.trim());
        }

        return credentialRepository.save(credential);
    }

    private String generateCredentialNumber(Long institutionId) {

        String credentialNumber;

        do {
            credentialNumber = credentialNumberGenerator.generate();
        } while (
                credentialRepository
                        .existsByInstitutionIdAndCredentialNumber(
                                institutionId,
                                credentialNumber
                        )
        );

        return credentialNumber;
    }
}