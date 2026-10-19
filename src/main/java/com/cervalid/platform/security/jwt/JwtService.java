package com.cervalid.platform.security.jwt;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_INSTITUTION_ID = "institutionId";
    private static final String CLAIM_INSTITUTION_USER_ID = "institutionUserId";
    private static final String CLAIM_TOKEN_TYPE = "tokenType";

    private static final String ACCESS_TOKEN = "ACCESS";
    private static final String REFRESH_TOKEN = "REFRESH";

    private static final String INSTITUTION_SELECTION_TOKEN =
            "INSTITUTION_SELECTION";

    private static final long INSTITUTION_SELECTION_EXPIRATION =
            5 * 60 * 1000L;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    // ACCESS TOKENS
    public String generateToken(InstitutionUser relation) {
        return buildToken(
                relation.getUser(),
                relation.getRole().getName().name(),
                relation.getInstitution().getId(),
                relation.getId(),
                ACCESS_TOKEN,
                jwtExpiration
        );
    }

    public String generateSuperAdminToken(User user) {
        return generateSuperAdminInstitutionToken(user, null);
    }

    public String generateSuperAdminInstitutionToken(
            User user,
            Long institutionId
    ) {
        return buildToken(
                user,
                RoleName.SUPER_ADMIN.name(),
                institutionId,
                null,
                ACCESS_TOKEN,
                jwtExpiration
        );
    }

    public String generateTokenWithoutInstitution(User user) {
        if (user.getGlobalRole() == null) {
            throw new IllegalStateException(
                    "El usuario no tiene un rol global configurado"
            );
        }

        return buildToken(
                user,
                user.getGlobalRole().name(),
                null,
                null,
                ACCESS_TOKEN,
                jwtExpiration
        );
    }

    public String generateInstitutionSelectionToken(User user) {
        return buildToken(
                user,
                INSTITUTION_SELECTION_TOKEN,
                null,
                null,
                INSTITUTION_SELECTION_TOKEN,
                INSTITUTION_SELECTION_EXPIRATION
        );
    }

    public boolean isInstitutionSelectionTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return INSTITUTION_SELECTION_TOKEN.equals(
                    claims.get(CLAIM_TOKEN_TYPE, String.class)
            );
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    // REFRESH TOKENS
    public String generateRefreshToken(InstitutionUser relation) {
        return buildToken(
                relation.getUser(),
                relation.getRole().getName().name(),
                relation.getInstitution().getId(),
                relation.getId(),
                REFRESH_TOKEN,
                jwtRefreshExpiration
        );
    }

    /**
     * Refresh token para el superadministrador.
     * institutionId puede ser null si está en contexto global.
     */
    public String generateSuperAdminRefreshToken(
            User user,
            Long institutionId
    ) {
        return buildToken(
                user,
                RoleName.SUPER_ADMIN.name(),
                institutionId,
                null,
                REFRESH_TOKEN,
                jwtRefreshExpiration
        );
    }

    private String buildToken(
            User user,
            String role,
            Long institutionId,
            Long institutionUserId,
            String tokenType,
            long expiration
    ) {
        var builder = Jwts.builder()
                .setSubject(user.getEmail())
                .claim(CLAIM_USER_ID, user.getId())
                .claim(CLAIM_ROLE, role)
                .claim(CLAIM_TOKEN_TYPE, tokenType)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + expiration)
                );

        if (institutionId != null) {
            builder.claim(CLAIM_INSTITUTION_ID, institutionId);
        }

        if (institutionUserId != null) {
            builder.claim(
                    CLAIM_INSTITUTION_USER_ID,
                    institutionUserId
            );
        }

        return builder.signWith(key).compact();
    }

    // =====================================================
    // VALIDATION AND CLAIMS
    // =====================================================

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return ACCESS_TOKEN.equals(
                    claims.get(CLAIM_TOKEN_TYPE, String.class)
            );
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);

            return REFRESH_TOKEN.equals(
                    claims.get(CLAIM_TOKEN_TYPE, String.class)
            );
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Long extractInstitutionId(String token) {
        Object value = extractAllClaims(token)
                .get(CLAIM_INSTITUTION_ID);

        return value != null
                ? Long.valueOf(value.toString())
                : null;
    }
}
