package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialIssuerResponse {

    private Long institutionId;
    private String institutionName;
}