package com.cervalid.platform.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionOption {

    private Long institutionUserId;
    private Long institutionId;
    private String institutionName;
    private String role;
}
