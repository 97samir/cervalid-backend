
package com.cervalid.platform.tenant.controller;

import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.tenant.dto.TenantBrandingDto;
import com.cervalid.platform.tenant.service.TenantBrandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class TenantBrandingController {

    private final TenantBrandingService brandingService;
    private final SecurityContextService securityContextService;

    @GetMapping("/tenant/branding")
    public TenantBrandingDto getBranding() {
        Long institutionId = securityContextService.getInstitutionId();

        if (institutionId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Debes seleccionar una institución para consultar su configuración."
            );
        }

        return brandingService.getBranding(institutionId);
    }
}
