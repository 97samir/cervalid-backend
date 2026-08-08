package com.cervalid.platform.institution.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInstitutionRequest {
    private String name;
    private String ruc;
    private Boolean active;
}
