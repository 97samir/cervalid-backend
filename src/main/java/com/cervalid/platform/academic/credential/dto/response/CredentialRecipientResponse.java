package com.cervalid.platform.academic.credential.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialRecipientResponse {

    private UUID studentPublicId;
    private String name;
    private String lastName;
    private String studentCode;
}