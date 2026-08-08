package com.cervalid.platform.config;

import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
@RequiredArgsConstructor
public class AuditConfig implements AuditorAware<Long> {

    private final SecurityContextService securityContextService;

    @Override
    public Optional<Long> getCurrentAuditor() {
        try {
            //return Optional.ofNullable(securityContextService.getUserId());
            Long userId = securityContextService.getUserId();

            System.out.println("AUDITOR USER ID = " + userId);

            return Optional.ofNullable(userId);

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}