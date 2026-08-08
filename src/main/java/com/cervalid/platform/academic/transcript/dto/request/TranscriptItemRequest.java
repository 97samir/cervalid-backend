package com.cervalid.platform.academic.transcript.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptItemRequest {

    @NotBlank
    private String courseCode;

    @NotBlank
    private String courseName;

    @NotNull
    @Positive
    private Integer credits;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    private BigDecimal grade;
}
