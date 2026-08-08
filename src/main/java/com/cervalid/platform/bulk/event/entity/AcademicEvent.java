package com.cervalid.platform.bulk.event.entity;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.bulk.event.enums.AcademicEventType;
import com.cervalid.platform.audit.entity.AuditableEntity;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "academic_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicEvent extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // pertenece a perfil
    @ManyToOne
    @JoinColumn(name = "academic_profile_id")
    private AcademicProfile academicProfile;

    // tipo de evento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcademicEventType type;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // JSON dinámico
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private JsonNode metadataJson;

    // orden por fecha de eventos creados
    @Column(name = "timeline_order")
    private Integer timelineOrder;

    private LocalDateTime date;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
    }
}