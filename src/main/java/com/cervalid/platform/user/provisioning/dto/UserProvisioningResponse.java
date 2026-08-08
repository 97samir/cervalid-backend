package com.cervalid.platform.user.provisioning.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProvisioningResponse {

    private Long userId;
    private String email;
    private boolean newlyCreated; // recien creado?
}