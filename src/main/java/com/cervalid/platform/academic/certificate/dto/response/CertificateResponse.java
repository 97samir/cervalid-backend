package com.cervalid.platform.academic.certificate.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponse {

    //private Long id;
    private UUID publicId;
    private String certificateNumber;

    private CertificateStatus status;
    private CertificateType type;

    private String title;
    private String description;
    private LocalDate awardedAt;

    private String certificateHash;
    private String verificationHash;
    private String blockchainTxHash;
    private String blockchainNetwork;
    private Long blockNumber;
    private LocalDateTime anchoredAt;

    private String verificationUrl;

    private String documentHash;
    private String documentUrl;

    private JsonNode snapshotJson;

    private LocalDateTime issuedAt;
    private LocalDateTime revokedAt;
    private LocalDateTime createdAt;

    // para dashboard
    private String studentName;
    private String studentCode;
    private AcademicProgram program;
    private AcademicFaculty faculty;
}