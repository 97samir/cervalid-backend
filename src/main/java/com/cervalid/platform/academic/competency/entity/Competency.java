package com.cervalid.platform.academic.competency.entity;

import com.cervalid.platform.academic.competency.enums.CompetencyEvidenceType;
import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "academic_competencies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Competency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;
    private Long institutionId;
    private Long studentId;
    private UUID studentPublicId;
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private CompetencyLevel level;

    @Enumerated(EnumType.STRING)
    private CompetencyStatus status;

    @Enumerated(EnumType.STRING)
    private CompetencySource source;

    private String issuer; // proviene de:
    private LocalDate acquiredDate; // fecha anterior en la fue emitida
    private UUID evidenceReference; // id

    @Enumerated(EnumType.STRING)
    private CompetencyEvidenceType evidenceType; // modulo: transcript etc
    // private boolean active;
}