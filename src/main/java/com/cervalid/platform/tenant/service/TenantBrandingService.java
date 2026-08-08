package com.cervalid.platform.tenant.service;

import com.cervalid.platform.tenant.dto.TenantBrandingDto;
import com.cervalid.platform.tenant.entity.Tenant;
import com.cervalid.platform.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantBrandingService {

    private final TenantRepository tenantRepository;

    public TenantBrandingDto getBranding(Long tenantId) {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado"));

        return TenantBrandingDto.builder()
                .tenantId(tenant.getId())
                .institutionName(tenant.getName())
                .logoUrl(tenant.getLogoUrl())
                .primaryColor(tenant.getPrimaryColor())
                .build();
    }
}
