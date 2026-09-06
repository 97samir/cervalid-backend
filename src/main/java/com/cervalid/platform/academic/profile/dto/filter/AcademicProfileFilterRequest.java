package com.cervalid.platform.academic.profile.dto.filter;

import lombok.Data;

@Data
public class AcademicProfileFilterRequest {

    //private Long institutionId;
    private String program;
    private String faculty;
    private Boolean active;
    private String academicPeriod;
}