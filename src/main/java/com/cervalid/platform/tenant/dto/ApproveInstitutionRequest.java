package com.cervalid.platform.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class ApproveInstitutionRequest {

    @NotBlank
    private String walletAddress;
}
