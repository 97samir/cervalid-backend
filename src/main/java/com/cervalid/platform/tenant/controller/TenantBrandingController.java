package com.cervalid.platform.tenant.controller;

import com.cervalid.platform.security.jwt.JwtService;
import com.cervalid.platform.tenant.dto.TenantBrandingDto;
import com.cervalid.platform.tenant.service.TenantBrandingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TenantBrandingController {

    private final TenantBrandingService brandingService;
    private final JwtService jwtService;

    @GetMapping("/tenant/branding")
    public TenantBrandingDto getBranding(HttpServletRequest request) {
        String token = jwtService.resolveToken(request);
        Long tenantId = jwtService.extractInstitutionId(token);
        return brandingService.getBranding(tenantId);
    }
}
