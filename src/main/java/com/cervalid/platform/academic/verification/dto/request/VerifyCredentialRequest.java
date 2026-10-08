package com.cervalid.platform.academic.verification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyCredentialRequest {

    @NotBlank(message = "El número de credential es obligatorio")
    private String credentialNumber;

    @NotBlank(message = "El hash de verificación es obligatorio")
    private String verificationHash;
}