package com.cervalid.platform.user.dto.response;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.common.validation.DocumentType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserResponse {

    private Long id;
    private UUID publicId;
    private Long institutionUserId; // agregado
    private UUID institutionUserPublicId;

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