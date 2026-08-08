package com.cervalid.platform.academic.profile.dto.request;

import lombok.Data;

@Data
public class UpdateAcademicProfileRequest {

    private String program;
    private String faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;
    private Boolean active;
}