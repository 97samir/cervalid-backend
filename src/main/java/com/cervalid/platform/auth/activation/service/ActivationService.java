package com.cervalid.platform.auth.activation.service;

import com.cervalid.platform.academic.student.service.StudentLifecycleService;
import com.cervalid.platform.academic.student.service.StudentManagementService;
import com.cervalid.platform.auth.activation.dto.ActivationRequest;
import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.auth.token.repository.ActivationTokenRepository;
import com.cervalid.platform.auth.token.service.ActivationTokenService;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final ActivationTokenService activationTokenService;
    private final InstitutionMembershipService institutionMembershipService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentLifecycleService studentLifecycleService;
    private final StudentManagementService studentManagementService;

    @Transactional
    public void activateAccount(ActivationRequest request) {

        try {

            ActivationToken token =
                    activationTokenService.validateToken(
                            request.getToken()
                    );

            User user = token.getUser();

            if (!user.isActive()) {
                // actualizar datos del usuario
                user.setName(request.getName());
                user.setLastName(request.getLastName());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setDocumentType(request.getDocumentType());
                user.setDocument(request.getDocument());
                user.setPhone(request.getPhone());
                user.setActive(true);

                userRepository.save(user);
            }

            institutionMembershipService.activateMembership(
                    user.getId(),
                    token.getInstitution().getId()
            );

            InstitutionUser membership =
                    institutionMembershipService
                            .getMembership(
                                    user.getId(),
                                    token.getInstitution().getId()
                            );

            studentManagementService.activateStudent(
                    membership.getId()
            );

            activationTokenService.markAsUsed(token);

        } catch (Exception e) {

            System.err.println("ERROR EN  activateAccount()");
            System.err.println(e.getMessage());

            throw new RuntimeException("Error activando cuenta: " + e.getMessage(), e);
        }
    }
}
