package com.cervalid.platform.user.dto;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.common.validation.DocumentHolder;
import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.common.validation.DocumentValid;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@DocumentValid
public class CreateUserRequest implements DocumentHolder {

    private DocumentType documentType;
    private String document;

    private String name;
    private String lastName;
    private String email;
    private String password;

    private String phone;
    private RoleName role;   // STUDENT, INSTITUTION_SUBADMIN, INSTITUTION_AUDITOR
    private Long institutionId;  // obligatorio para multi-tenant
}
