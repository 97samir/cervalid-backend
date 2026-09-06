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

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(nullable = false)
    private Long institutionId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetencyLevel level;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetencyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CompetencySource source;

    private String issuer;

    private LocalDate acquiredDate;

    private UUID evidenceReference;

    @Enumerated(EnumType.STRING)
    private CompetencyEvidenceType evidenceType;

    private String academicPeriod;
}