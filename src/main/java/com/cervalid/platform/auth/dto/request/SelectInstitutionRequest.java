package com.cervalid.platform.auth.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectInstitutionRequest {

    private Long userId;
    private Long institutionUserId;
    private Long institutionId;
}
