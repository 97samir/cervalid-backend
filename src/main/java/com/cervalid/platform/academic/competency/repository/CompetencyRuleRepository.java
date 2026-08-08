package com.cervalid.platform.academic.competency.repository;

import com.cervalid.platform.academic.competency.entity.CompetencyRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetencyRuleRepository
        extends JpaRepository<CompetencyRuleEntity, Long> {

    Optional<CompetencyRuleEntity>
    findByInstitutionIdAndCourseCodeAndActiveTrue(
            Long institutionId,
            String courseCode
    );
}
