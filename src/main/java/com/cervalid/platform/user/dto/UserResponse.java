package com.cervalid.platform.user.dto;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.common.validation.DocumentType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private Long institutionUserId; // agregado

    private String name;
    private String lastName;
    private String email;

    private DocumentType documentType;
    private String document;
    private String phone;

    private Boolean active;
    private RoleName role;
    private Long institutionId;

}