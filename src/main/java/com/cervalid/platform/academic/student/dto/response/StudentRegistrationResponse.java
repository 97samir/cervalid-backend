package com.cervalid.platform.academic.student.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegistrationResponse {

    private UUID studentPublicId;
    private String email;
    private String studentCode;
    private String status;
}
