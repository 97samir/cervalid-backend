package com.cervalid.platform.academic.competency.entity;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "academic_competency_rules")
@Getter
@Setter
@Builder
public class CompetencyRuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long institutionId;
    private String courseCode;
    private String competencyName;
    private String description;

    @Enumerated(EnumType.STRING)
    private CompetencyLevel level;

    private boolean active = true;
}
