package com.cervalid.platform.academic.transcript.entity;

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

    private UUID publicId;
    private Long studentId;

    @Column(nullable = false)
    private Long institutionId;

    private String academicPeriod; // 2026-I

    @Enumerated(EnumType.STRING)
    private TranscriptStatus status;

    private String transcriptHash; // para verificación
    private LocalDateTime issuedAt;
}