package com.cervalid.platform.academic.verification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCertificateRequest {

    @NotBlank(message = "El número de certificado es obligatorio")
    private String certificateNumber;

    @NotBlank(message = "El hash de verificación es obligatorio")
    private String verificationHash;

}