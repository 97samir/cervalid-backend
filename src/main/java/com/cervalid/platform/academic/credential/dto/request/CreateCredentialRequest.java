package com.cervalid.platform.academic.credential.dto.request;

import com.cervalid.platform.academic.credential.enums.CredentialType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCredentialRequest {

    @NotNull(message = "El tipo de credential es obligatorio")
    private CredentialType type;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    private String title;

    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String description;

    @NotNull(message = "La fecha de otorgamiento es obligatoria")
    @PastOrPresent(message = "La fecha de otorgamiento no puede ser futura")
    private LocalDate awardedAt;
}