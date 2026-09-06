package com.cervalid.platform.academic.transcript.dto.response;

import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
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

    private AcademicPeriodType academicPeriodType;
    private String academicPeriod;

    private String status;
    private String hash;

    private Integer coursesCount;
    private BigDecimal gpa;
    private Integer creditsEarned;
    private Integer creditsFailed;
}
