package com.cervalid.platform.academic.certificate.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDetailResponse {

    private CertificateResponse certificate;

    private String transcriptHash;
    private String studentCode;

    private String network;
    private Long blockNumber;
    private LocalDateTime anchoredAt;
}