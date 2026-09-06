package com.cervalid.platform.academic.student.dto.request;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterStudentRequest {

    // USER
    private String email;

    //private Long institutionId; // MEMBERSHIP
    private String studentCode; // STUDENT

    // PROFILE
    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    //private String advisor; // tutot
    private Integer currentCycle;
    private LocalDate admissionDate;
    private LocalDate graduationDate;

}