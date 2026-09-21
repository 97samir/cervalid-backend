package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.RevokeCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.UpdateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.response.CredentialDocumentResponse;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialManagementService {

    private final CredentialRepository credentialRepository;
    private final StudentRepository studentRepository;
    private final CredentialValidationService credentialValidationService;
    private final CredentialTimelineService credentialTimelineService;
    private final CredentialNumberGenerator credentialNumberGenerator;
    private final CredentialDocumentValidationService documentValidationService;

    @Transactional
    public Credential create(
            Long institutionId,
            UUID studentPublicId,
            CreateCredentialRequest request
    ) {

        Student student = studentRepository
                .findByPublicIdAndInstitutionId(
                        studentPublicId,
                        institutionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El estudiante no existe en la institución actual")
                );

        credentialValidationService.validateCreate(
                institutionId,
                student,
                request
        );

        String credentialNumber = credentialNumberGenerator.generate();

        Credential credential = Credential.builder()
                .institutionId(institutionId)
                .studentId(student.getId())
                .credentialNumber(credentialNumber)
                .type(request.getType())
                .title(request.getTitle().trim())
                .description(request.getDescription()
                        != null ? request.getDescription().trim()
                        : null)
                .awardedAt(request.getAwardedAt())
                .status(CredentialStatus.DRAFT)
                .deleted(false)
                .build();

        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential update(
            Long institutionId,
            UUID publicId,
            UpdateCredentialRequest request
    ) {

        Credential credential =
                getCredential(institutionId, publicId);

        credentialValidationService.validateUpdate(credential, request);

        credential.setTitle(request.getTitle().trim());

        credential.setDescription(
                request.getDescription() != null
                        ? request.getDescription().trim()
                        : null
        );

        return credentialRepository.save(credential);
    }

    @Transactional
    public Credential issue(
            Long institutionId,
            UUID publicId
    ) {

        Credential credential =
                getCredential(institutionId, publicId);

        credentialValidationService.validateIssue(credential);
        credential.setStatus(CredentialStatus.ISSUED);
        credential.setIssuedAt(LocalDateTime.now());

        Credential issuedCredential =
                credentialRepository.save(credential);

        Student student =
                getStudent(
                        institutionId,
                        credential.getStudentId()
                );

        credentialTimelineService.credentialIssued(
                issuedCredential,
                student
        );

        return issuedCredential;
    }

    @Transactional
    public Credential revoke(
            Long institutionId,
            UUID publicId,
            RevokeCredentialRequest request
    ) {

        Credential credential =
                getCredential(institutionId, publicId);

        credentialValidationService.validateRevoke(credential);
        credential.setStatus(CredentialStatus.REVOKED);
        credential.setRevokedAt(LocalDateTime.now());
        credential.setRevocationReason(request.getReason().trim());

        Credential revokedCredential =
                credentialRepository.save(credential);

        Student student =
                getStudent(
                        institutionId,
                        credential.getStudentId()
                );

        credentialTimelineService.credentialRevoked(
                revokedCredential,
                student
        );

        return revokedCredential;
    }

    private Credential getCredential(
            Long institutionId,
            UUID publicId
    ) {

        return credentialRepository
                .findByPublicIdAndInstitutionIdAndDeletedFalse(
                        publicId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El credential no existe"
                        )
                );
    }

    private Student getStudent(
            Long institutionId,
            Long studentId
    ) {

        return studentRepository
                .findByIdAndInstitutionId(
                        studentId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El estudiante no pertenece a la institución actual"
                        )
                );
    }

    @Transactional
    public CredentialDocumentResponse updateDocument(
            Long institutionId,
            UUID publicId,
            MultipartFile file,
            String documentHash,
            String documentUrl
    ) {

        Credential credential =
                getCredential(
                        institutionId,
                        publicId
                );

        String finalDocumentHash =
                documentValidationService.validateAndResolveHash(
                        file,
                        documentHash,
                        documentUrl
                );

        // se actualiza hash cuando solamente haya nueva data
        if (finalDocumentHash != null
                && !finalDocumentHash.isBlank()) {

            credential.setDocumentHash(
                    finalDocumentHash.trim()
            );
        }
        // url independiente actualizacion
        if (documentUrl != null
                && !documentUrl.isBlank()) {

            credential.setDocumentUrl(
                    documentUrl.trim()
            );
        }

        Credential saved =
                credentialRepository.save(credential);

        return CredentialDocumentResponse.builder()
                .credentialPublicId(saved.getPublicId().toString())
                .documentHash(saved.getDocumentHash())
                .documentUrl(saved.getDocumentUrl())
                .build();
    }
}