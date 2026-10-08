package com.cervalid.platform.academic.verification.dto.view;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VerificationHistoryView {

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