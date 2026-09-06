package com.cervalid.platform.academic.timeline.entity;

import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "academic_timeline_events")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private Long institutionId;

    @Column(nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimelineEventType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimelineEventSource source;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    private UUID referenceId; // enlace

    @Enumerated(EnumType.STRING)
    private TimelineReferenceType referenceType;

    private String academicPeriod;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode metadataJson;
}