package com.cervalid.platform.academic.competency.engine;

import com.cervalid.platform.academic.competency.entity.CompetencyRuleEntity;
import com.cervalid.platform.academic.competency.repository.CompetencyRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TranscriptCompetencyRuleProvider {

    private final CompetencyRuleRepository repository;

    public Optional<CompetencyRule> findRule(
            Long institutionId,
            String courseCode) {

        return repository
                .findByInstitutionIdAndCourseCodeAndActiveTrue(
                        institutionId,
                        courseCode
                )
                .map(this::toDomain);
    }

    private CompetencyRule toDomain(
            CompetencyRuleEntity entity) {

        return new CompetencyRule(
                entity.getCourseCode(),
                entity.getCompetencyName(),
                entity.getDescription(),
                entity.getLevel()
        );
    }
}