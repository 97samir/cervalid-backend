package com.cervalid.platform.invitation.dto;

import com.cervalid.platform.common.enums.RoleName;
import lombok.*;

@Data
@Getter
@Setter
public class InvitationRequest {

    private String email;
    private RoleName role;
    private Long institutionId;
}