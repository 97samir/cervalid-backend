package com.cervalid.platform.academic.credential.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevokeCredentialRequest {

    @NotBlank(message = "El motivo de revocación es obligatorio")
    @Size(max = 1000, message = "El motivo de revocación no puede superar los 1000 caracteres")
    private String reason;
}