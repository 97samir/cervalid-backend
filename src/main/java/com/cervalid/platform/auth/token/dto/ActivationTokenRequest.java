package com.cervalid.platform.auth.token.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationTokenRequest {

    private Long userId;
    private Long institutionId;
    private Long invitedBy;
}