package com.cervalid.platform.academic.profile.dto.internal;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAcademicProfileCommand {

    private Long studentId;
    private Long institutionId;
    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
    private String advisor;
    private String academicPeriod;

}