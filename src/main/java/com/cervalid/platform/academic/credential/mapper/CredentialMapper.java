package com.cervalid.platform.academic.credential.mapper;

import com.cervalid.platform.academic.credential.dto.response.CredentialDetailResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialResponse;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.student.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class CredentialMapper {

    public CredentialResponse toResponse(
            Credential credential,
            Student student
    ) {
        return CredentialResponse.builder()
                .publicId(credential.getPublicId())
                .studentPublicId(student.getPublicId())
                .credentialNumber(credential.getCredentialNumber())
                .type(credential.getType())
                .title(credential.getTitle())
                .description(credential.getDescription())
                .awardedAt(credential.getAwardedAt())
                .issuedAt(credential.getIssuedAt())
                .status(credential.getStatus())
                .revokedAt(credential.getRevokedAt())
                .build();
    }

    public CredentialDetailResponse toDetailResponse(
            Credential credential,
            Student student
    ) {
        return CredentialDetailResponse.builder()
                .publicId(credential.getPublicId())
                .studentPublicId(student.getPublicId())
                .credentialNumber(credential.getCredentialNumber())
                .type(credential.getType())
                .title(credential.getTitle())
                .description(credential.getDescription())
                .awardedAt(credential.getAwardedAt())
                .issuedAt(credential.getIssuedAt())
                .status(credential.getStatus())
                .documentHash(credential.getDocumentHash())
                .documentUrl(credential.getDocumentUrl())
                .blockchainTxHash(credential.getBlockchainTxHash())
                .blockchainNetwork(credential.getBlockchainNetwork())
                .blockNumber(credential.getBlockNumber())
                .anchoredAt(credential.getAnchoredAt())
                .revokedAt(credential.getRevokedAt())
                .revocationReason(credential.getRevocationReason())
                .build();
    }
}