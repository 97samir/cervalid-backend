package com.cervalid.platform.user.dto;

import com.cervalid.platform.common.enums.RoleName;
import lombok.*;

@Getter
@Setter
public class UpdateUserRoleRequest {

    private RoleName role;
    private Long institutionId;

}
