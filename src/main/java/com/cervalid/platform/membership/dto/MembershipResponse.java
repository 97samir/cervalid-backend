package com.cervalid.platform.membership.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MembershipResponse {

    private Long institutionUserId;
    private Long userId;
    private Long institutionId;
    private String role;
    private boolean active;
}