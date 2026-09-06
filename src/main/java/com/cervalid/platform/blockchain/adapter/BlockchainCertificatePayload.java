package com.cervalid.platform.blockchain.adapter;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
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
    private String title;
    private LocalDate awardedAt;
    private String documentHash;
    private LocalDateTime issuedAt;
    private String network;

    public Map<String, Object> toMap() {

        Map<String, Object> payload =
                new LinkedHashMap<>();

        payload.put("certificatePublicId", certificatePublicId);
        payload.put("certificateNumber", certificateNumber);
        payload.put("certificateHash", certificateHash);
        payload.put("transcriptHash", transcriptHash);
        payload.put("verificationHash", verificationHash);
        payload.put("studentId", studentId);
        payload.put("institutionId", institutionId);
        payload.put("title", title);
        payload.put("awardedAt", awardedAt);
        payload.put("documentHash", documentHash);
        payload.put("issuedAt", issuedAt);

        return payload;
    }
}