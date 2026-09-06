package com.cervalid.platform.academic.profile.dto.request;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.Data;

@Data
public class UpdateAcademicProfileRequest {

    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;
    private Boolean active;
    private String academicPeriod;
}