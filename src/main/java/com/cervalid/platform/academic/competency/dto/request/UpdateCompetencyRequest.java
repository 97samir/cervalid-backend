package com.cervalid.platform.academic.competency.dto.request;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateCompetencyRequest {

    private String name;
    private String description;
    private CompetencyLevel level;
    private String issuer;
    private LocalDate acquiredDate;
}