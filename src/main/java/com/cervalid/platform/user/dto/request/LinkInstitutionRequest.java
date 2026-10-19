package com.cervalid.platform.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkInstitutionRequest {

    private String email;
    private Long institutionId;
    private String role;
}
