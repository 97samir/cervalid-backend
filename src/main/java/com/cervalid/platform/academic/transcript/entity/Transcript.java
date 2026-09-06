package com.cervalid.platform.academic.transcript.entity;

import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "academic_transcripts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transcript {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long institutionId;

    @Column(nullable = false)
    private String academicPeriod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicPeriodType academicPeriodType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TranscriptStatus status;

    @Column
    private String transcriptHash;

    private LocalDateTime issuedAt;
}