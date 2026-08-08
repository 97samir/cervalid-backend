package com.cervalid.platform.tenant.dto;

import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.common.validation.DocumentValid;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@DocumentValid
public class InstitutionRequestResponse {

    private Long id;
    private String institutionName;
    private String ruc;
    private String institutionType;

    private String country;
    private String city;
    private String address;

    private String name;
    private String lastName;

    private DocumentType documentType;
    private String document;
    private String phone;
    private String position;

    private String contactEmail;
    private String documentAcreditationUrl;

    private String website;
    private String description;
    private String institutionalEmail;
    private String institutionalDominio;
    private String status;

}
