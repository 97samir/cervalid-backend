package com.cervalid.platform.institution.dto;

import lombok.*;

@Data
@Getter
@Setter
@Builder
public class InstitutionResponse {

    private Long id;
    private String name;
    private String ruc;
    private String type;
    private String country;
    private String city;
    private String address;

    private boolean tienePresenciaDigital;
    private String website;
    private String description;
    private String institutionalEmail;
    private String institutionalDominio;

    private Boolean active;
}
