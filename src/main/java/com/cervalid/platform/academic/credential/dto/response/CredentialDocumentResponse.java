package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDocumentResponse {

    private String credentialPublicId;
    private String documentHash;
    private String documentUrl;
}