
package com.cervalid.platform.auth.service;

import com.cervalid.platform.auth.dto.response.AuthTokenResponse;
import com.cervalid.platform.auth.dto.response.InstitutionOption;
import com.cervalid.platform.auth.dto.response.LoginResponse;
import com.cervalid.platform.auth.dto.request.SelectInstitutionRequest;
import com.cervalid.platform.auth.entity.UserRefreshToken;
import com.cervalid.platform.auth.mapper.AuthMapper;
import com.cervalid.platform.auth.repository.UserRefreshTokenRepository;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.security.jwt.JwtService;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final UserRefreshTokenRepository refreshTokenRepository;

    private final AuthMapper authMapper;
    private final SecurityContextService securityContextService;
    private final JwtService jwtService;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpirationMillis;

    // LOGIN
    @Transactional
    public LoginResponse login(String email, String password) {
        if (email == null || email.isBlank()
                || password == null || password.isBlank()) {
            throw new IllegalArgumentException(
                    "Correo y contraseña son obligatorios"
            );
        }

        String normalizedEmail = email.trim();

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                normalizedEmail,
                                password
                        )
                );

        User user = userRepository.findByEmailIgnoreCase(
                        authentication.getName()
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Usuario autenticado no encontrado"
                        )
                );

        if (!user.isActive()) {
            throw new IllegalStateException("Usuario inactivo");
        }

        // Superadministrador: el rol global es la fuente de verdad.
        if (user.getGlobalRole() == RoleName.SUPER_ADMIN) {
            String accessToken =
                    jwtService.generateSuperAdminToken(user);

            String refreshToken =
                    persistRefreshToken(
                            user,
                            jwtService.generateSuperAdminRefreshToken(
                                    user,
                                    null
                            )
                    );

            return LoginResponse.superAdmin(
                    accessToken,
                    refreshToken
            );
        }

        List<InstitutionUser> relations =
                institutionUserRepository
                        .findByUserIdAndActiveTrue(user.getId());

        if (relations.isEmpty()) {
            throw new IllegalStateException(
                    "El usuario no tiene instituciones activas"
            );
        }

        if (relations.size() == 1) {
            InstitutionUser relation = relations.get(0);

            String accessToken =
                    jwtService.generateToken(relation);

            String refreshToken =
                    persistRefreshToken(
                            user,
                            jwtService.generateRefreshToken(relation)
                    );

            return LoginResponse.singleInstitution(
                    accessToken,
                    refreshToken
            );
        }

        List<InstitutionOption> options =
                authMapper.toInstitutionOptions(relations);

        String selectionToken =
                jwtService.generateInstitutionSelectionToken(user);

        // validar seleccion multiple
        return LoginResponse.multiInstitution(
                user.getId(),
                options,
                selectionToken
        );
    }

    // LOGOUT
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return;
        }

        refreshTokenRepository
                .findByTokenAndRevokedFalse(refreshToken)
                .ifPresent(savedToken -> {
                    savedToken.setRevoked(true);
                    refreshTokenRepository.save(savedToken);
                });
    }

    // SELECT INSTITUTION
    @Transactional
    public AuthTokenResponse selectInstitution(
            SelectInstitutionRequest request,
            String selectionToken
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "La solicitud es obligatoria"
            );
        }

        Long currentUserId;

        if (selectionToken != null && !selectionToken.isBlank()) {
            if (!jwtService.isInstitutionSelectionTokenValid(
                    selectionToken
            )) {
                throw new IllegalArgumentException(
                        "El desafío de selección es inválido o ha vencido"
                );
            }

            var claims = jwtService.extractAllClaims(selectionToken);

            currentUserId = readOptionalLongClaim(claims, "userId");

            if (currentUserId == null || currentUserId <= 0) {
                throw new IllegalArgumentException(
                        "El desafío no contiene un usuario válido"
                );
            }

            User challengeUser = userRepository.findById(currentUserId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Usuario no encontrado"
                    ));

            if (!challengeUser.isActive()
                    || !challengeUser.getEmail().equalsIgnoreCase(
                    claims.getSubject()
            )) {
                throw new IllegalArgumentException(
                        "El desafío no corresponde a un usuario activo"
                );
            }
        } else {
            // Permite el flujo existente del superadministrador,
            // que ya tiene una sesión autenticada.
            currentUserId = securityContextService.getUserId();

            if (currentUserId == null) {
                throw new IllegalStateException(
                        "Se requiere un desafío de selección válido "
                                + "o una sesión autenticada"
                );
            }
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Usuario no encontrado"
                        )
                );

        // Superadministrador que selecciona una institución.
        if (currentUser.getGlobalRole() == RoleName.SUPER_ADMIN
                && request.getInstitutionUserId() == null
                && request.getInstitutionId() != null) {

            // Validar que la institución exista.
            // Se implementará con InstitutionRepository en el siguiente ajuste.
            String accessToken =
                    jwtService.generateSuperAdminInstitutionToken(
                            currentUser,
                            request.getInstitutionId()
                    );

            String refreshToken = persistRefreshToken(
                    currentUser,
                    jwtService.generateSuperAdminRefreshToken(
                            currentUser,
                            request.getInstitutionId()
                    )
            );

            return new AuthTokenResponse(accessToken, refreshToken);
        }

        if (request.getInstitutionUserId() == null) {
            throw new IllegalArgumentException(
                    "institutionUserId es obligatorio"
            );
        }

        InstitutionUser relation =
                institutionUserRepository
                        .findByIdWithInstitutionAndRole(
                                request.getInstitutionUserId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Membresía no encontrada"
                                )
                        );

        if (!relation.isActive()) {
            throw new IllegalStateException(
                    "La membresía institucional está inactiva"
            );
        }

        // Evita seleccionar la membresía de otro usuario.
        if (!relation.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException(
                    "La membresía no pertenece al usuario autenticado"
            );
        }

        // Si el cliente también envía institutionId, debe coincidir.
        if (request.getInstitutionId() != null
                && !request.getInstitutionId().equals(
                relation.getInstitution().getId()
        )) {
            throw new IllegalArgumentException(
                    "La institución no coincide con la membresía"
            );
        }

        String accessToken = jwtService.generateToken(relation);

        String refreshToken = persistRefreshToken(
                currentUser,
                jwtService.generateRefreshToken(relation)
        );

        return new AuthTokenResponse(accessToken, refreshToken);
    }

    // EXIT INSTITUTION
    @Transactional
    public AuthTokenResponse exitInstitution() {
        Long userId = securityContextService.getUserId();

        if (userId == null) {
            throw new IllegalStateException(
                    "El usuario no está autenticado"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Usuario no encontrado"
                        )
                );

        if (user.getGlobalRole() != RoleName.SUPER_ADMIN) {
            throw new IllegalStateException(
                    "Solo el superadministrador puede salir del contexto institucional"
            );
        }

        String accessToken =
                jwtService.generateTokenWithoutInstitution(user);

        String refreshToken = persistRefreshToken(
                user,
                jwtService.generateSuperAdminRefreshToken(user, null)
        );

        return new AuthTokenResponse(accessToken, refreshToken);
    }

    // REFRESH TOKEN
    @Transactional
    public AuthTokenResponse refreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()
                || !jwtService.isRefreshTokenValid(refreshToken)) {
            throw new IllegalArgumentException(
                    "Refresh token inválido o vencido"
            );
        }

        UserRefreshToken savedToken =
                refreshTokenRepository
                        .findByTokenAndRevokedFalse(refreshToken)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Refresh token revocado o inexistente"
                                )
                        );

        if (!savedToken.getExpiresAt().isAfter(Instant.now())) {
            savedToken.setRevoked(true);
            throw new IllegalArgumentException(
                    "Refresh token vencido"
            );
        }

        var claims = jwtService.extractAllClaims(refreshToken);

        User user = savedToken.getUser();

        Long tokenUserId = Long.valueOf(
                claims.get("userId").toString()
        );

        if (!user.getId().equals(tokenUserId)
                || !user.isActive()) {
            savedToken.setRevoked(true);

            throw new IllegalArgumentException(
                    "El refresh token no corresponde a un usuario activo"
            );
        }

        String role = claims.get("role", String.class);
        Long institutionId = readOptionalLongClaim(
                claims, "institutionId"
        );
        Long institutionUserId = readOptionalLongClaim(
                claims, "institutionUserId"
        );

        String accessToken;
        String replacementRefreshToken;

        if (user.getGlobalRole() == RoleName.SUPER_ADMIN) {
            if (!RoleName.SUPER_ADMIN.name().equals(role)) {
                throw new IllegalArgumentException(
                        "Rol de refresh token inválido"
                );
            }

            accessToken =
                    jwtService.generateSuperAdminInstitutionToken(
                            user,
                            institutionId
                    );

            replacementRefreshToken =
                    jwtService.generateSuperAdminRefreshToken(
                            user,
                            institutionId
                    );
        } else {
            if (institutionUserId == null || institutionId == null) {
                throw new IllegalArgumentException(
                        "El refresh token no contiene el contexto institucional"
                );
            }

            InstitutionUser relation =
                    institutionUserRepository
                            .findByIdWithInstitutionAndRole(
                                    institutionUserId
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Membresía institucional no encontrada"
                                    )
                            );

            boolean validContext =
                    relation.isActive()
                            && relation.getUser().getId().equals(user.getId())
                            && relation.getInstitution().getId()
                            .equals(institutionId)
                            && relation.getRole().getName().name()
                            .equals(role);

            if (!validContext) {
                throw new IllegalArgumentException(
                        "El contexto institucional del refresh token ya no es válido"
                );
            }

            accessToken = jwtService.generateToken(relation);

            replacementRefreshToken =
                    jwtService.generateRefreshToken(relation);
        }

        savedToken.setRevoked(true);

        String persistedReplacement = persistRefreshToken(
                user,
                replacementRefreshToken
        );

        return new AuthTokenResponse(
                accessToken,
                persistedReplacement
        );
    }

    // PRIVATE HELPERS
    private String persistRefreshToken(
            User user,
            String token
    ) {
        // un solo refresh token por usuario
        refreshTokenRepository.deleteByUser(user);

        Instant expiresAt = Instant.now().plusMillis(
                refreshExpirationMillis
        );

        refreshTokenRepository.save(
                UserRefreshToken.builder()
                        .user(user)
                        .token(token)
                        .expiresAt(expiresAt)
                        .revoked(false)
                        .build()
        );

        return token;
    }

    private Long readOptionalLongClaim(
            io.jsonwebtoken.Claims claims,
            String claimName
    ) {
        Object value = claims.get(claimName);

        if (value == null) {
            return null;
        }

        try {
            return Long.valueOf(value.toString());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Claim numérico inválido: " + claimName,
                    ex
            );
        }
    }
}
