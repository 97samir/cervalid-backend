package com.cervalid.platform.academic.transcript.dto.response;

import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDetailResponse {

    private UUID publicId;

    private AcademicPeriodType academicPeriodType;
    private String academicPeriod;

    private String status;
    private String hash;
    private BigDecimal gpa;
    private Integer creditsEarned;
    private Integer creditsFailed;
    private Integer coursesCount;
    private LocalDateTime issuedAt;
    private List<TranscriptItemDTO> items;
}