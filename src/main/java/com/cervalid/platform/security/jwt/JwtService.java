package com.cervalid.platform.security.jwt;

import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    private Key key;

    @PostConstruct
    public void init(){
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(InstitutionUser relation) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", relation.getUser().getId());
        claims.put("role", relation.getRole().getName().toString());
        claims.put("institutionId", relation.getInstitution().getId());
        claims.put("institutionUserId", relation.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(relation.getUser().getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    public String generateSuperAdminToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("role", "SUPER_ADMIN");
        claims.put("institutionId", null);

        System.out.println("CLAIMS FULL: " + claims);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + jwtExpiration)
                )
                .signWith(key)
                .compact();
    }

    // para entrar bajo contexto a una institucion
    public String generateSuperAdminInstitutionToken(User user, Long institutionId) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", "SUPER_ADMIN") // clave
                //.claim("role", "INSTITUTION_ADMIN") // clave
                .claim("institutionId", institutionId)
                //.claim("institutionId", null)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    // generar token sin instituion (caso superadmin bajo contexto de login)
    public String generateTokenWithoutInstitution(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("role", user.getGlobalRole().name())
                .claim("institutionId", null) // clave
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

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
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String generateRefreshToken(User user){

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration)) // 7 días
                .signWith(key)
                .compact();
    }

    // Extraer institutionId (tenant) del token
    public Long extractInstitutionId(String token) {
        Object institutionId = extractAllClaims(token).get("institutionId");
        return institutionId != null ? Long.valueOf(institutionId.toString()) : null;
    }

    // Obtener token desde header Authorization: Bearer xxx
    public String resolveToken(jakarta.servlet.http.HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}

