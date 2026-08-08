package com.cervalid.platform.academic.competency.mapper;

import com.cervalid.platform.academic.competency.dto.response.CompetencyResponse;
import com.cervalid.platform.academic.competency.entity.Competency;
import org.springframework.stereotype.Component;

@Component
public class CompetencyMapper {

    public CompetencyResponse toResponse(
            Competency competency) {

        return CompetencyResponse.builder()
                .publicId(competency.getPublicId())
                .studentPublicId(competency.getStudentPublicId())
                //.institutionId(competency.getInstitutionId())
                .name(competency.getName())
                .description(competency.getDescription())
                .level(competency.getLevel())
                .status(competency.getStatus())
                .source(competency.getSource())
                .issuer(competency.getIssuer())
                .acquiredDate(competency.getAcquiredDate())
                .evidenceReference(competency.getEvidenceReference())
                .evidenceType(competency.getEvidenceType())
                .build();
    }
}