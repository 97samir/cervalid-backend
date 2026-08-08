package com.cervalid.platform.security.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final Long institutionUserId;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    // Para auditoría y otros módulos
    public Map<String, Object> getClaims() {

        Map<String, Object> claims = new HashMap<>();

        if (userId != null) {
            claims.put("userId", userId);
        }

        if (institutionUserId != null) {
            claims.put("institutionUserId", institutionUserId);
        }

        if (email != null) {
            claims.put("email", email);
        }

        return claims;
    }
    // ========== UserDetails ==========
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}