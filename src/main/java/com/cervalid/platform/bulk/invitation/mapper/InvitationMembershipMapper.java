package com.cervalid.platform.bulk.invitation.mapper;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import org.springframework.stereotype.Component;

@Component
public class InvitationMembershipMapper {

    public CreateMembershipRequest toMembershipRequest(
            InvitationBulkRowRequest row,
            Long userId,
            Long institutionId) {

        return CreateMembershipRequest.builder()
                .userId(userId)
                .institutionId(institutionId)
                .role(RoleName.valueOf(row.getRole()))
                .active(false)
                .build();
    }
}