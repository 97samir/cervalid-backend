package com.cervalid.platform.blockchain.adapter;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlockchainCertificatePayload {

    private UUID certificatePublicId;
    private String certificateNumber;
    private String certificateHash;
    private String transcriptHash;
    private String verificationHash;
    private Long studentId;
    private Long institutionId;
    private LocalDateTime issuedAt;
    private String network; // e.g. "POLYGON", "ETHEREUM"

    public Map<String, Object> toMap() {
        return Map.of(
                "certificatePublicId", certificatePublicId.toString(),
                "certificateNumber", certificateNumber,
                "certificateHash", certificateHash,
                "transcriptHash", transcriptHash,
                "verificationHash", verificationHash,
                "studentId", studentId,
                "institutionId", institutionId,
                "issuedAt", issuedAt.toString(),
                "network", network
        );
    }
}