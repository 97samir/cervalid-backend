package com.cervalid.platform.academic.certificate.mapper;

import com.cervalid.platform.academic.certificate.dto.response.CertificateDetailResponse;
import com.cervalid.platform.academic.certificate.dto.response.CertificateResponse;
import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.user.entity.User;
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
                .blockchainNetwork(certificate.getNetwork())
                .blockNumber(certificate.getBlockNumber())
                .awardedAt(certificate.getAwardedAt())
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

    // mapeo para dashboard
    public CertificateResponse toResponse(
            Certificate certificate,
            Student student,
            User user,
            AcademicProfile profile
    ) {
        CertificateResponse response = toResponse(certificate);

        response.setStudentCode(
                student != null ?
                        student.getStudentCode() : null);

        response.setStudentName(user != null ? (
                        (user.getName() != null ? user.getName() : "")
                                + " "
                                + (user.getLastName() != null ? user.getLastName() : "")
                ).trim() : null);

        response.setProgram(
                profile != null ?
                        profile.getProgram() : null);

        response.setFaculty(
                profile != null ?
                        profile.getFaculty() : null);

        return response;
    }

    // mapeo para certificado
    public CertificateDetailResponse toResponseDetail(
            Certificate certificate,
            CertificateResponse response) {

        return CertificateDetailResponse.builder()

                .certificate(response)
                .transcriptHash(certificate.getTranscriptHash())
                .certificateHash(certificate.getCertificateHash())
                .verificationHash(certificate.getVerificationHash())
                .blockchainTxHash(certificate.getBlockchainTxHash())
                .blockchainNetwork(certificate.getNetwork())
                .blockNumber(certificate.getBlockNumber())
                .anchoredAt(certificate.getAnchoredAt())
                .studentCode(response.getStudentCode()
                        != null
                        ? response.getStudentCode()
                        : certificate.getSnapshotJson()
                        .path("student")
                        .path("studentCode")
                        .asText(null))
                .build();
    }

}