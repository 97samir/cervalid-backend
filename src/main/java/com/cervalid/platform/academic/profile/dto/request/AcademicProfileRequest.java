package com.cervalid.platform.academic.profile.dto.request;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProfileRequest {

    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor; // tutor
    private String academicPeriod;
}