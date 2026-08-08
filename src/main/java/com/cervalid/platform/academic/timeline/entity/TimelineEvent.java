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

    private UUID publicId;
    private Long institutionId;
    private Long studentId;

    @Enumerated(EnumType.STRING)
    private TimelineEventType type;

    @Enumerated(EnumType.STRING)
    private TimelineEventSource source;

    private String title;

    @Column(length = 3000)
    private String description;

    private LocalDateTime eventDate;
    private UUID referenceId; // enlace

    @Enumerated(EnumType.STRING)
    private TimelineReferenceType referenceType;

    @Builder.Default
    private Boolean deleted = false;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode metadataJson;
}