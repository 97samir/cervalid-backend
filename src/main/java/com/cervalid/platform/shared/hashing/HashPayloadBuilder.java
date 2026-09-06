package com.cervalid.platform.shared.hashing;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class HashPayloadBuilder {

    public Map<String, Object> buildTranscriptHashPayload(
            Long studentId,
            Long institutionId,
            String academicPeriod,
            String transcriptHash) {

        return Map.of(
                "type", "TRANSCRIPT",
                "studentId", studentId,
                "institutionId", institutionId,
                "academicPeriod", academicPeriod,
                "transcriptHash", transcriptHash,
                "timestamp", LocalDateTime.now().toString()
        );
    }

    public Map<String, Object> buildCertificateHashPayload(
            Long studentId,
            Long institutionId,
            String certificateNumber,
            String transcriptHash,
            String title,
            String awardedAt,
            String issuedAt) {

        return Map.of(
                "type", "CERTIFICATE",
                "studentId", studentId,
                "institutionId", institutionId,
                "certificateNumber", certificateNumber,
                "transcriptHash", transcriptHash,
                "title", title,
                "awardedAt", awardedAt,
                "issuedAt", issuedAt
        );
    }
}