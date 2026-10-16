package com.cervalid.platform.academic.certificate.mapper;

import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import org.springframework.stereotype.Component;

@Component
public class CertificateMapper {

    public CertificateResponse toResponse(
            Certificate certificate) {

        return CertificateResponse.builder()
                //.id(certificate.getId())
                .publicId(certificate.getPublicId())
                .certificateNumber(certificate.getCertificateNumber())
                .status(certificate.getStatus())
                .type(certificate.getType())
                .title(certificate.getTitle())
                .description(certificate.getDescription())
                .awardedAt(certificate.getAwardedAt())
                .certificateHash(certificate.getCertificateHash())
                .blockchainTxHash(certificate.getBlockchainTxHash())
                .verificationHash(certificate.getVerificationHash())
                .verificationUrl(certificate.getVerificationUrl())
                .documentHash(certificate.getDocumentHash())
                .documentUrl(certificate.getDocumentUrl())
                .snapshotJson(certificate.getSnapshotJson())
                .issuedAt(certificate.getIssuedAt())
                .revokedAt(certificate.getRevokedAt())
                .createdAt(certificate.getCreatedAt())
                .build();
    }

    public CertificateDetailResponse toResponseDetail(Certificate certificate) {

        return CertificateDetailResponse.builder()

                .certificate(toResponse(certificate))
                .transcriptHash(certificate.getTranscriptHash())
                .network(certificate.getNetwork())
                .blockNumber(certificate.getBlockNumber())
                .anchoredAt(certificate.getAnchoredAt())
                .studentCode(certificate.getSnapshotJson()
                                .path("student")
                                .path("studentCode")
                                .asText(null))
                .build();
    }

}