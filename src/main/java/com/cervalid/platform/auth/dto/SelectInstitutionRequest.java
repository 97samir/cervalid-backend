package com.cervalid.platform.auth.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SelectInstitutionRequest {

    private Long userId;
    private Long institutionUserId;
    private Long institutionId;
}
