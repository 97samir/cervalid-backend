package com.cervalid.platform.academic.certificate.dto.request;

import com.cervalid.platform.academic.certificate.enums.CertificateType;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueCertificateRequest {

    // private Long studentId; // ya se obtiene del transcriptId
    private UUID transcriptPublicId;
    private CertificateType type;
    // private String description;
}