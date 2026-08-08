package com.cervalid.platform.auth.service;

import com.cervalid.platform.auth.dto.*;
import com.cervalid.platform.auth.entity.UserRefreshToken;
import com.cervalid.platform.auth.repository.UserRefreshTokenRepository;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.security.jwt.JwtService;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final JwtService jwtService;
    private final UserRefreshTokenRepository refreshTokenRepository;
    private final SecurityContextService securityContextService;

    @Value("${app.superadmin.email}")
    private String superAdminEmail;

    // LOGIN PRINCIPAL
    public LoginResponse login(String email, String password) {

        String normalizedEmail = email.toLowerCase().trim();

        // autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, password)
        );

        // obtener usuario global
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        // caso SUPER ADMIN
        if (user.getEmail().equalsIgnoreCase(superAdminEmail)) {

            String token = jwtService.generateSuperAdminToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return LoginResponse.superAdmin(token, refreshToken);
        }

        // buscar instituciones activas
        List<InstitutionUser> relations =
                institutionUserRepository.findByUserIdAndActiveTrue(user.getId());

        if (relations.isEmpty()) {
            throw new RuntimeException("Usuario sin institución activa");
        }
        // si solo tiene una institución -> generar token directo
        if (relations.size() == 1) {

            String token = jwtService.generateToken(relations.get(0));
            String refreshToken = jwtService.generateRefreshToken(user);
            return LoginResponse.singleInstitution(token, refreshToken);
        }

        // si tiene varias -> devolver opciones
        List<InstitutionOption> options = relations.stream()
                .map(rel -> {

                    InstitutionOption option = new InstitutionOption();

                    option.setInstitutionUserId(rel.getId());
                    option.setInstitutionId(rel.getInstitution().getId());
                    option.setInstitutionName(rel.getInstitution().getName());
                    option.setRole(rel.getRole().getName().toString());

                    return option;

                }).toList();

        return LoginResponse.multiInstitution(user.getId(), options);
    }

    // SELECCIÓN DE INSTITUCIÓN
    @Transactional
    public AuthTokenResponse selectInstitution(SelectInstitutionRequest request) {

        System.out.println("=== SELECT INSTITUTION DEBUG ===");
        System.out.println("Request payload: " + request);

        if (request == null) {
            throw new RuntimeException("Request no puede ser nulo");
        }

        // SUPER_ADMIN
        if (request.getInstitutionUserId() == null && request.getInstitutionId() != null) {

            System.out.println("Entrando como SUPER_ADMIN");

            // usar security context para el superadmin
            Long userId = securityContextService.getUserId();

            if (userId == null) {
                throw new RuntimeException("No se pudo obtener el usuario desde el token");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String token = jwtService.generateSuperAdminInstitutionToken(
                    user,
                    request.getInstitutionId()
            );

            String refreshToken = saveRefreshToken(user);

            return new AuthTokenResponse(token, refreshToken);
        }

        // USUARIO NORMAL
        if (request.getInstitutionUserId() == null) {
            throw new RuntimeException("institutionUserId es requerido");
        }

        System.out.println("Entrando como USUARIO NORMAL");

        InstitutionUser relation = institutionUserRepository
                .findByIdWithInstitutionAndRole(request.getInstitutionUserId())
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));

        if (!relation.isActive()) {
            throw new RuntimeException("Institución desactivada");
        }

        User user = relation.getUser();

        String token = jwtService.generateToken(relation);
        String refreshToken = saveRefreshToken(user);

        return new AuthTokenResponse(token, refreshToken);
    }

    // metodo para salir de instituion bajo contexto (caso superadmin)
    @Transactional
    public AuthTokenResponse exitInstitution() {

        Long userId = securityContextService.getUserId();

        if (userId == null) {
            throw new RuntimeException("Usuario no autenticado");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getGlobalRole() == null) {
            throw new RuntimeException("El usuario no tiene rol global");
        }
        // generar token SIN institutionId
        String token = jwtService.generateTokenWithoutInstitution(user);
        String refreshToken = saveRefreshToken(user);

        return new AuthTokenResponse(token, refreshToken);
    }

    @Transactional
    public AuthTokenResponse refreshToken(String refreshToken){
        // Validar refresh token en DB
        UserRefreshToken savedToken = refreshTokenRepository.findByTokenAndRevokedFalse(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        User user = savedToken.getUser();

        // Revocar token antiguo
        savedToken.setRevoked(true);
        refreshTokenRepository.save(savedToken);
        // Caso SUPER_ADMIN
        if(user.getEmail().equalsIgnoreCase(superAdminEmail)){
            String newToken = jwtService.generateSuperAdminToken(user);
            String newRefreshToken = saveRefreshToken(user); // guarda nuevo refresh token en DB
            return new AuthTokenResponse(newToken, newRefreshToken);
        }
        // Usuario institucional
        InstitutionUser relation = institutionUserRepository
                .findFirstByUserIdAndActiveTrue(user.getId())
                .orElseThrow(() -> new RuntimeException("Usuario sin institución activa"));

        String newToken = jwtService.generateToken(relation);
        String newRefreshToken = saveRefreshToken(user); // nuevo refresh token en DB

        return new AuthTokenResponse(newToken, newRefreshToken);
    }

    // metodo para renovar refresh token para el frontend
    private String saveRefreshToken(User user) {
        // borrar tokens antiguos
        refreshTokenRepository.deleteByUser(user);

        String refreshToken = jwtService.generateRefreshToken(user);
        UserRefreshToken entity = UserRefreshToken.builder()
                .user(user)
                .token(refreshToken)
                .expiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 30)) // 30 días
                .revoked(false)
                .build();
        refreshTokenRepository.save(entity);

        return refreshToken;
    }

}