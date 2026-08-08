package com.cervalid.platform.membership.dto;

import com.cervalid.platform.common.enums.RoleName;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMembershipRequest {

    private Long userId;
    private Long institutionId;
    private RoleName role;
    private Boolean active;
}