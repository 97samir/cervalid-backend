package com.cervalid.platform.tenant.dto;

import com.cervalid.platform.common.validation.DocumentHolder;
import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.common.validation.DocumentValid;
import lombok.*;

@Setter
@Getter
@DocumentValid
public class InstitutionRequestDTO implements DocumentHolder {

    private DocumentType documentType;
    private String document;

    private String name;
    private String lastName;
    private String phone;

    private String institutionName;
    private String ruc;
    private String institutionType;
    private String country;
    private String city;
    private String address;

    private String position;
    private String documentAcreditationUrl;

    private String contactEmail;
    private String password;

    private Boolean tienePresenciaDigital;
    private String website;
    private String institutionalEmail;
    private String institutionalDominio;
    private String description;

    private String status;
}
