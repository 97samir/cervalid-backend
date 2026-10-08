package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.RevokeCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.UpdateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.response.CredentialDetailResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialDocumentResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialVerificationResponse;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.mapper.CredentialMapper;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialManagementService credentialManagementService;
    private final CredentialQueryService credentialQueryService;
    private final CredentialMapper credentialMapper;
    private final StudentRepository studentRepository;

    public CredentialResponse create(
            Long institutionId,
            UUID studentPublicId,
            CreateCredentialRequest request
    ) {

        Credential credential =
                credentialManagementService.create(
                        institutionId,
                        studentPublicId,
                        request
                );

        Student student =
                studentRepository
                        .findByPublicIdAndInstitutionId(
                                studentPublicId,
                                institutionId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El estudiante no existe en la institución actual"
                                )
                        );

        return credentialMapper.toResponse(
                credential,
                student
        );
    }

    public CredentialResponse update(
            Long institutionId,
            UUID publicId,
            UpdateCredentialRequest request
    ) {

        Credential credential =
                credentialManagementService.update(
                        institutionId,
                        publicId,
                        request
                );

        Student student =
                getStudent(
                        institutionId,
                        credential.getStudentId()
                );

        return credentialMapper.toResponse(
                credential,
                student
        );
    }

    public CredentialResponse issue(
            Long institutionId,
            UUID publicId
    ) {

        Credential credential =
                credentialManagementService.issue(
                        institutionId,
                        publicId
                );

        Student student =
                getStudent(
                        institutionId,
                        credential.getStudentId()
                );

        return credentialMapper.toResponse(
                credential,
                student
        );
    }

    public CredentialResponse revoke(
            Long institutionId,
            UUID publicId,
            RevokeCredentialRequest request
    ) {

        Credential credential =
                credentialManagementService.revoke(
                        institutionId,
                        publicId,
                        request
                );

        Student student =
                getStudent(
                        institutionId,
                        credential.getStudentId()
                );

        return credentialMapper.toResponse(
                credential,
                student
        );
    }

    public CredentialDetailResponse getByPublicId(
            Long institutionId,
            UUID publicId
    ) {

        return credentialQueryService.getByPublicId(
                institutionId,
                publicId
        );
    }

    public Page<CredentialResponse> getByStudent(
            Long institutionId,
            UUID studentPublicId,
            Pageable pageable
    ) {

        return credentialQueryService.getByStudent(
                institutionId,
                studentPublicId,
                pageable
        );
    }

    public Page<CredentialResponse> search(
            Long institutionId,
            CredentialFilterRequest filter,
            Pageable pageable
    ) {

        return credentialQueryService.search(
                institutionId,
                filter,
                pageable
        );
    }

    public Credential getEntity(
            Long institutionId,
            UUID publicId
    ) {

        return credentialQueryService.getEntity(
                institutionId,
                publicId
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

    public CredentialDocumentResponse updateDocument(
            Long institutionId,
            UUID publicId,
            MultipartFile file,
            String documentHash,
            String documentUrl
    ) {

        return credentialManagementService.updateDocument(
                institutionId,
                publicId,
                file,
                documentHash,
                documentUrl
        );
    }

    public CredentialVerificationResponse getVerificationByPublicId(
            UUID publicId
    ) {

        return credentialQueryService
                .getVerificationByPublicId(publicId);
    }
}