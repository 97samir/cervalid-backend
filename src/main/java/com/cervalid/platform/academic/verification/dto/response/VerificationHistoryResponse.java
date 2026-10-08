package com.cervalid.platform.academic.verification.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationHistoryResponse {

    private UUID publicId;

    /* Recurso verificado */
    private String verificationType;

    private UUID resourcePublicId;
    private String resourceNumber;
    private String resourceType;

    /* Información académica */
    private String studentName;
    private String institutionName;

    /* Resultado de la verificación */
    private String verificationReason;
    private String status;
    private String verificationSource;

    /* Auditoría */
    private String ip;
    private LocalDateTime verifiedAt;
}