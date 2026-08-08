package com.cervalid.platform.bulk.validation.business.validators;

import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExistingMembershipValidator {

    private final UserRepository userRepository;
    private final InstitutionUserRepository institutionUserRepository;

    public String validate(String email, Long institutionId) {

        return userRepository.findByEmail(email)
                .map(User::getId)
                .flatMap(userId ->
                        institutionUserRepository
                                .findByUserIdAndInstitutionId(
                                        userId,
                                        institutionId
                                )
                )
                .map(relation ->
                        "Usuario ya pertenece a institución"
                )
                .orElse(null);
    }
}