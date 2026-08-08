package com.cervalid.platform.academic.competency.engine;

import com.cervalid.platform.academic.competency.enums.CompetencyLevel;

public record CompetencyRule(

        String courseCode,
        String competencyName,
        String description,
        CompetencyLevel level
) { }