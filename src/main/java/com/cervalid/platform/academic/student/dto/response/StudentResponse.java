package com.cervalid.platform.academic.student.dto.response;

import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {

    private UUID publicId;
    private String studentCode;
    private String status;
    private LocalDate admissionDate;
    private LocalDate graduationDate;
    private AcademicProgram program;
    //private Long institutionId;

    // datos de user
    private String name;
    private String lastName;
    private String fullName;
    private String email;
    private String document;
    private String phone;
}