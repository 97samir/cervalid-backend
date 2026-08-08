package com.cervalid.platform.academic.certificate.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
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

    private String hash;
    private String blockchainTxHash;
    private String verificationHash;
    private String verificationUrl;

    private JsonNode snapshotJson;

    private LocalDateTime issuedAt;
    private LocalDateTime revokedAt;
}