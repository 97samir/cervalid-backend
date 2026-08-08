package com.cervalid.platform.tenant.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TenantBrandingDto {

    private Long tenantId;
    private String institutionName;
    private String logoUrl;
    private String primaryColor;
}
