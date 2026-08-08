package com.cervalid.platform.academic.student.dto.response;

import com.cervalid.platform.academic.profile.dto.response.AcademicProfileResponse;
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
public class StudentDetailResponse {

    private UUID publicId;

    //private Long userId;
    //private Long institutionId;
    //private Long institutionMembershipId;

    private String studentCode;
    private String status;

    private LocalDate admissionDate;
    private LocalDate graduationDate;
    //private Boolean deleted;

    private AcademicProfileResponse profile;

    // datos de user
    private String name;
    private String lastName;
    private String fullName;
    private String email;
    private String document;
    private String phone;
}
