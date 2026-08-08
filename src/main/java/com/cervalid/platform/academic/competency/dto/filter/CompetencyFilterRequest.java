package com.cervalid.platform.academic.competency.dto.filter;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class CompetencyFilterRequest {

    private UUID studentPublicId;
    private String name;
    private CompetencyLevel level;
    private CompetencyStatus status;
}