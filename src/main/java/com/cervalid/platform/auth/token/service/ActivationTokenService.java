package com.cervalid.platform.auth.token.service;

import com.cervalid.platform.auth.token.dto.ActivationTokenRequest;
import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.auth.token.repository.ActivationTokenRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivationTokenService {

    private final ActivationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    public ActivationToken createToken(
            ActivationTokenRequest request) {

        User user = userRepository.findById(
                request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Institution institution =
                institutionRepository.findById(
                        request.getInstitutionId())
                        .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        User invitedBy = null;

        if (request.getInvitedBy() != null) {
            invitedBy = userRepository.findById(
                    request.getInvitedBy())
                    .orElseThrow(() ->
                            new RuntimeException("Inviter not found"));
        }

        invalidatePreviousTokens(
                user.getId(),
                institution.getId()
        );

        ActivationToken token = new ActivationToken();

        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setInstitution(institution);
        token.setInvitedBy(invitedBy);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(1));
        token.setUsed(false);

        return tokenRepository.save(token);
    }

    // metodo para validar si se ya se envio al mismo correo
    public ActivationToken getOrCreateToken(
            Long userId,
            Long institutionId,
            Long invitedBy) {

        Optional<ActivationToken> existing =
                tokenRepository.findTopByUserIdAndInstitutionIdOrderByCreatedAtDesc(
                        userId,
                        institutionId
                );

        if (existing.isPresent()) {

            ActivationToken token = existing.get();

            // si no ha expirado, reutilizar
            if (!token.isUsed()
                    && token.getExpiresAt().isAfter(LocalDateTime.now())) {

                return token;
            }

            // si expiró o fue usado → invalidar y regenerar
            token.setUsed(true);
            tokenRepository.save(token);
        }

        return createToken(
                ActivationTokenRequest.builder()
                        .userId(userId)
                        .institutionId(institutionId)
                        .invitedBy(invitedBy)
                        .build()
        );
    }

    public boolean hasActiveToken(
            Long userId, Long institutionId) {
        return tokenRepository
                .existsByUser_IdAndInstitution_IdAndUsedFalse(
                        userId, institutionId);
    }

    public ActivationToken validateToken(String tokenValue) {

        ActivationToken token =
                tokenRepository.findByToken(tokenValue)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid token"));

        if (token.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        return token;
    }

    @Transactional
    public void markAsUsed(ActivationToken token) {
        token.setUsed(true);
        tokenRepository.save(token);}

    @Transactional
    public ActivationToken regenerateToken(
            Long userId,
            Long institutionId) {

        invalidatePreviousTokens(userId, institutionId);

        return createToken(
                ActivationTokenRequest.builder()
                        .userId(userId)
                        .institutionId(institutionId)
                        .build()
        );
    }

    private void invalidatePreviousTokens(
            Long userId,
            Long institutionId) {

        List<ActivationToken> tokens =
                tokenRepository
                        .findAllByUser_IdAndInstitution_IdAndUsedFalse(
                                userId,
                                institutionId
                        );

        tokens.forEach(token -> token.setUsed(true));
        tokenRepository.saveAll(tokens);
    }
}