package com.cervalid.platform.academic.transcript.dto.item;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptItemDTO {

    //private Long id;
    private UUID publicId;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private BigDecimal grade;

    // mas adelante
    private String term; // 2026-I
}
