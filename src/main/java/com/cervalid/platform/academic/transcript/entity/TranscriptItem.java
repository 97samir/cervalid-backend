package com.cervalid.platform.academic.transcript.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "academic_transcript_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;
    private Long transcriptId;
    private String courseCode;
    private String courseName;
    private Integer credits;
    private BigDecimal grade;
}