package com.cervalid.platform.auth.service;

import com.cervalid.platform.auth.dto.response.InstitutionOption;
import com.cervalid.platform.auth.dto.response.MeResponse;
import com.cervalid.platform.auth.mapper.AuthMapper;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService {

    private final UserRepository userRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final InstitutionRepository institutionRepository;
    private final AuthMapper authMapper;

    public MeResponse getCurrentUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        Long institutionId = UserContext.getInstitutionId();
        String institutionName = null;

        if (institutionId != null) {
            institutionName = institutionRepository.findById(institutionId)
                    .map(institution -> institution.getName())
                    .orElse(null);
        }

        String role = UserContext.getRole() != null
                ? UserContext.getRole().name()
                : null;

        return authMapper.toMeResponse(
                user,
                role,
                institutionId,
                UserContext.getInstitutionUserId(),
                institutionName
        );
    }

    public List<InstitutionOption> getMyInstitutions(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Usuario no encontrado"
                        )
                );

        List<InstitutionUser> relations =
                institutionUserRepository.findByUserIdAndActiveTrue(
                        user.getId()
                );

        return authMapper.toInstitutionOptions(relations);
    }
}
