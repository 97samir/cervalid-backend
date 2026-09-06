package com.cervalid.platform.academic.profile.dto.response;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AcademicProfileResponse {

    private UUID publicId;
    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;
    private Boolean active;
    private String academicPeriod;
}