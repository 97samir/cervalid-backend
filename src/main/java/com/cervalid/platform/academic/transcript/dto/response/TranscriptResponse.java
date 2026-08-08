package com.cervalid.platform.academic.transcript.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptResponse {

    private UUID certificatePublicId;
    private UUID publicId;
    private String academicPeriod;
    private String status;
    private String hash;

    private Integer coursesCount;
    private BigDecimal gpa;
    private Integer creditsEarned;
}
