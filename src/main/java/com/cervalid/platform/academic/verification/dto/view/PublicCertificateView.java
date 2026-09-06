package com.cervalid.platform.academic.verification.dto.view;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.*;

@Getter
@Builder
public class PublicCertificateView {

    private String studentName;
    private String institutionName;

    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
}