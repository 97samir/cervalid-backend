package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.RevokeCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.UpdateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.response.*;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService {

    private final CredentialManagementService credentialManagementService;
    private final CredentialQueryService credentialQueryService;

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

        return credentialQueryService.getResponseByPublicId(
                institutionId,
                credential.getPublicId()
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

        return credentialQueryService.getResponseByPublicId(
                institutionId,
                credential.getPublicId()
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

        return credentialQueryService.getResponseByPublicId(
                institutionId,
                credential.getPublicId()
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

        return credentialQueryService.getResponseByPublicId(
                institutionId,
                credential.getPublicId()
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


    public CredentialDashboardSummaryResponse getDashboardSummary(
            Long institutionId
    ) {
        return credentialQueryService.getDashboardSummary(institutionId);
    }

}