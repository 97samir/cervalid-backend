package com.cervalid.platform.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UpdateUserRequest {

    private String name;
    private String lastName;
    private String email;
    private String document;
    private String phone;
    private Long institutionId;
    private Boolean active;
}
