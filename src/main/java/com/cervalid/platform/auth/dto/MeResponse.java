package com.cervalid.platform.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeResponse {

    private Long userId;
    private String email;
    private String name;
    private String lastName;

    private String document;
    private String phone;

    private String role;
    private Long institutionId;
    private Long institutionUserId;
    private String institutionName;

}