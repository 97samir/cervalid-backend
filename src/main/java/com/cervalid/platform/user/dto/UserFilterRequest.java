package com.cervalid.platform.user.dto;

import com.cervalid.platform.common.enums.RoleName;
import lombok.Data;

@Data
public class UserFilterRequest {
    private Long institutionId;
    private RoleName role;
    private Boolean active;
}
