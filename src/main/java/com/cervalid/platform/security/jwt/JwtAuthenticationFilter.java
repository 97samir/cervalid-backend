
package com.cervalid.platform.security.jwt;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.security.service.CustomUserDetailsService;
import com.cervalid.platform.security.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private static final String AUTH_COOKIE = "cervalid_token";

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (token != null && !token.isBlank()) {
                authenticateRequest(token, request);
            }

        } catch (
                JwtException |
                IllegalArgumentException |
                AuthenticationException ex
        ) {
            SecurityContextHolder.clearContext();
            UserContext.clear();

            log.debug(
                    "Autenticación JWT rechazada: {}",
                    ex.getClass().getSimpleName()
            );
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }

    private void authenticateRequest(
            String token,
            HttpServletRequest request
    ) {
        // Solo se permiten tokens de acceso, no refresh tokens.
        if (!jwtService.isTokenValid(token)) {
            throw new IllegalArgumentException(
                    "Token de acceso inválido o expirado"
            );
        }

        Claims claims = jwtService.extractAllClaims(token);

        String email = claims.getSubject();
        String role = claims.get("role", String.class);

        if (email == null || email.isBlank()
                || role == null || role.isBlank()) {
            throw new IllegalArgumentException(
                    "Claims de autenticación incompletos"
            );
        }

        Long userId = readLongClaim(claims, "userId");
        Long institutionId =
                readOptionalLongClaim(claims, "institutionId");
        Long institutionUserId =
                readOptionalLongClaim(claims, "institutionUserId");

        if (userId <= 0
                || (institutionId != null && institutionId <= 0)
                || (institutionUserId != null && institutionUserId <= 0)) {
            throw new IllegalArgumentException(
                    "Claims de identificación inválidos"
            );
        }

        CustomUserDetails userDetails =
                userDetailsService.loadUserByUsernameAndContext(
                        email,
                        userId,
                        institutionUserId,
                        institutionId,
                        role
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        UserContext.setUserId(userDetails.getUserId());
        UserContext.setInstitutionUserId(
                userDetails.getInstitutionUserId()
        );
        UserContext.setRole(RoleName.valueOf(role));

        if (institutionId != null) {
            UserContext.setInstitutionId(institutionId);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization != null
                && authorization.startsWith("Bearer ")) {

            String bearerToken = authorization.substring(7).trim();

            if (!bearerToken.isEmpty()) {
                return bearerToken;
            }
        }

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (AUTH_COOKIE.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private Long readLongClaim(Claims claims, String name) {
        Long value = readOptionalLongClaim(claims, name);

        if (value == null) {
            throw new IllegalArgumentException(
                    "Falta el claim obligatorio: " + name
            );
        }

        return value;
    }

    private Long readOptionalLongClaim(Claims claims, String name) {
        Object value = claims.get(name);

        if (value == null) {
            return null;
        }

        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Claim numérico inválido: " + name,
                    ex
            );
        }
    }
}
