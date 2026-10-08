package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialAcademicResponse {

    private String program;
    private String faculty;
    private String modality;
}