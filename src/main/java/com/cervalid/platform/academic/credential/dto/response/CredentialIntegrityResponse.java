package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialIntegrityResponse {

    private String credentialHash;
    private String verificationHash;

    private String blockchainTxHash;
    private String blockchainNetwork;
    private Long blockNumber;
    private LocalDateTime anchoredAt;
}