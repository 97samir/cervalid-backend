package com.cervalid.platform.academic.credential.mapper;

import com.cervalid.platform.academic.credential.dto.response.*;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CredentialMapper {

    public CredentialResponse toResponse(
            Credential credential,
            Student student,
            User user,
            AcademicProfile profile
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
                .createdAt(credential.getCreatedAt())

                .studentName(user == null
                        ? null
                        : buildStudentName(user))
                .studentCode(student.getStudentCode())

                .program(profile == null || profile.getProgram() == null
                        ? null
                        : profile.getProgram().name())
                .faculty(profile == null || profile.getFaculty() == null
                        ? null
                        : profile.getFaculty().name())

                .build();
    }

    private String buildStudentName(User user) {
        String name = user.getName() == null ? "" : user.getName().trim();
        String lastName = user.getLastName() == null
                ? ""
                : user.getLastName().trim();

        return (name + " " + lastName).trim();
    }

    public CredentialDetailResponse toDetailResponse(
            Credential credential,
            Student student,
            User user,
            AcademicProfile profile,
            Institution institution
    ) {

        CredentialRecipientResponse recipient =
                CredentialRecipientResponse.builder()
                        .studentPublicId(student.getPublicId())
                        .name(user.getName())
                        .lastName(user.getLastName())
                        .studentCode(student.getStudentCode())
                        .build();

        CredentialIssuerResponse issuer =
                CredentialIssuerResponse.builder()
                        .institutionId(institution.getId())
                        .institutionName(institution.getName())
                        .build();

        CredentialAcademicResponse academic =
                CredentialAcademicResponse.builder()
                        .program(profile.getProgram() != null
                                        ? profile.getProgram().toString()
                                        : null)
                        .faculty(profile.getFaculty() != null
                                        ? profile.getFaculty().toString()
                                        : null)
                        .modality(profile.getModality())
                        .build();

        CredentialDocumentResponse document =
                CredentialDocumentResponse.builder()
                        .credentialPublicId(
                                credential.getPublicId().toString())
                        .documentHash(
                                credential.getDocumentHash())
                        .documentUrl(
                                credential.getDocumentUrl())
                        .build();

        CredentialIntegrityResponse integrity =
                CredentialIntegrityResponse.builder()

                        .credentialHash(
                                credential.getCredentialHash())
                        .verificationHash(
                                credential.getVerificationHash())
                        .blockchainTxHash(
                                credential.getBlockchainTxHash())
                        .blockchainNetwork(
                                credential.getBlockchainNetwork())
                        .blockNumber(
                                credential.getBlockNumber())
                        .anchoredAt(
                                credential.getAnchoredAt())
                        .build();

        CredentialRevocationResponse revocation = null;

        if (credential.getStatus() == CredentialStatus.REVOKED) {

            revocation =
                    CredentialRevocationResponse.builder()
                            .revokedAt(credential.getRevokedAt())
                            .reason(credential.getRevocationReason())
                            .build();
        }

        return CredentialDetailResponse.builder()
                .publicId(credential.getPublicId())
                .credentialNumber(credential.getCredentialNumber())
                .type(credential.getType())
                .title(credential.getTitle())
                .description(credential.getDescription())
                .awardedAt(credential.getAwardedAt())
                .issuedAt(credential.getIssuedAt())
                .status(credential.getStatus())
                .recipient(recipient)
                .issuer(issuer)
                .academic(academic)
                .document(document)
                .integrity(integrity)
                .revocation(revocation)
                .verificationUrl(credential.getVerificationUrl())
                .build();
    }

    public CredentialVerificationResponse toVerificationResponse(
            Credential credential,
            Student student,
            User user,
            AcademicProfile profile,
            Institution institution
    ) {

        CredentialRecipientResponse recipient =
                CredentialRecipientResponse.builder()
                        .studentPublicId(student.getPublicId())
                        .name(user.getName())
                        .lastName(user.getLastName())
                        .studentCode(student.getStudentCode())
                        .build();

        CredentialIssuerResponse issuer =
                CredentialIssuerResponse.builder()
                        .institutionId(institution.getId())
                        .institutionName(institution.getName())
                        .build();

        CredentialAcademicResponse academic =
                CredentialAcademicResponse.builder()
                        .program(profile.getProgram() != null
                                        ? profile.getProgram().toString()
                                        : null)
                        .faculty(profile.getFaculty() != null
                                        ? profile.getFaculty().toString()
                                        : null)
                        .modality(profile.getModality())
                        .build();

        CredentialDocumentResponse document =
                CredentialDocumentResponse.builder()
                        .credentialPublicId(
                                credential.getPublicId().toString())
                        .documentHash(
                                credential.getDocumentHash())
                        .documentUrl(
                                credential.getDocumentUrl())
                        .build();

        CredentialIntegrityResponse integrity =
                CredentialIntegrityResponse.builder()

                        .credentialHash(
                                credential.getCredentialHash())
                        .verificationHash(
                                credential.getVerificationHash())
                        .blockchainTxHash(
                                credential.getBlockchainTxHash())
                        .blockchainNetwork(
                                credential.getBlockchainNetwork())
                        .blockNumber(
                                credential.getBlockNumber())
                        .anchoredAt(
                                credential.getAnchoredAt())
                        .build();

        CredentialRevocationResponse revocation = null;

        if (credential.getStatus() == CredentialStatus.REVOKED) {

            revocation =
                    CredentialRevocationResponse.builder()
                            .revokedAt(credential.getRevokedAt())
                            .reason(credential.getRevocationReason())
                            .build();
        }

        return CredentialVerificationResponse.builder()
                .credentialPublicId(credential.getPublicId())
                .credentialNumber(credential.getCredentialNumber())
                .type(credential.getType())
                .title(credential.getTitle())
                .description(credential.getDescription())
                .awardedAt(credential.getAwardedAt())
                .issuedAt(credential.getIssuedAt())
                .status(credential.getStatus())
                .recipient(recipient)
                .issuer(issuer)
                .academic(academic)
                .document(document)
                .integrity(integrity)
                .revocation(revocation)
                .verificationUrl(credential.getVerificationUrl())
                .build();
    }
}