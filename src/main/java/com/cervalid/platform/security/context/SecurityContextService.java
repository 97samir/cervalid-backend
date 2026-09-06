package com.cervalid.platform.security.context;

import com.cervalid.platform.security.user.CustomUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextService {

    public Long getUserId() {
        String value = getClaim("userId");
        return value!= null ? Long.valueOf(value) : null;
    }

    public String getEmail() {
        return getClaim("email");
    }

    public Long getInstitutionId() {

        Long id = UserContext.getInstitutionId();

        if (id != null) {
            return id;
        }

        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth != null &&
                auth.getPrincipal() instanceof CustomUserDetails user) {

            Object claim = user.getClaims().get("institutionId");

            if (claim != null) {
                try {
                    return Long.parseLong(claim.toString());
                } catch (NumberFormatException ignored) {
                }
            }
        }

        return null;
    }

    public String getWalletAddress() {
        return getClaim("walletAddress");
    }

    private String getClaim(String key) {
        var auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !(
                auth.getPrincipal()
                        instanceof CustomUserDetails details)) {
            return null;
        }
        if (details.getClaims() == null){
            return null;
        }
        Object value = details.getClaims().get(key);
        return value != null ? value.toString() : null;
    }
}
