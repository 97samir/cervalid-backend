package com.cervalid.platform.auth.activation.dto;

import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.common.validation.DocumentValid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@DocumentValid
public class ActivationRequest {

    private String token;
    private String name;
    private String lastName;
    private String password;
    private DocumentType documentType;
    private String document;
    private String phone;
}
