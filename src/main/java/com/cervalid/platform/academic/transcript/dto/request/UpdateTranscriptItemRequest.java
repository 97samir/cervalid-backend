package com.cervalid.platform.academic.transcript.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateTranscriptItemRequest {

    @NotBlank
    private String courseName;

    @NotNull
    @Positive
    private Integer credits;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private Double grade;
}