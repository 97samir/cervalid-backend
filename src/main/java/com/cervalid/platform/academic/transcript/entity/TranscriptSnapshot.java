package com.cervalid.platform.academic.transcript.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "academic_transcript_snapshots")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long transcriptId;

    @Column(columnDefinition = "TEXT")
    private String snapshotJson;

    private String hash;
    private LocalDateTime createdAt;
}
