package com.cervalid.platform.bulk.validation.business.validators;

import com.cervalid.platform.auth.token.repository.ActivationTokenRepository;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingPendingInvitationValidator {

    private final UserRepository userRepository;
    private final ActivationTokenRepository tokenRepository;

    public String validate(String email, Long institutionId) {

        return userRepository.findByEmail(email)
                .flatMap(user ->
                        tokenRepository
                                .findTopByUserIdAndInstitutionIdOrderByCreatedAtDesc(
                                        user.getId(),
                                        institutionId
                                )
                )
                .filter(token -> !token.isUsed())
                .map(token ->
                        "Ya existe invitación pendiente"
                )
                .orElse(null);
    }
}
