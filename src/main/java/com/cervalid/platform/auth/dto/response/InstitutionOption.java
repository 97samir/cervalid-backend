package com.cervalid.platform.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    public class InstitutionOption {

    // representa multiple de instituciones disponibles
    private Long institutionUserId;
    private Long institutionId;
    private String institutionName;
    private String role;
}
