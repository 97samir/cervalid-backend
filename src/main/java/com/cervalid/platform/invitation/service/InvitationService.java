package com.cervalid.platform.invitation.service;

import com.cervalid.platform.auth.token.dto.ActivationTokenRequest;
import com.cervalid.platform.auth.token.service.ActivationTokenService;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.auth.token.repository.ActivationTokenRepository;
import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.invitation.dto.InvitationRequest;
import com.cervalid.platform.invitation.dto.InvitationResponse;
import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.notification.email.EmailService;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.repository.RoleRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.provisioning.dto.UserProvisioningRequest;
import com.cervalid.platform.user.provisioning.service.UserProvisioningService;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;

    private final UserProvisioningService userProvisioningService;
    private final InstitutionMembershipService institutionMembershipService;
    private final ActivationTokenService activationTokenService;
    private final ActivationTokenRepository activationTokenRepository;

    private final InvitationEmailService invitationEmailService;

    // para usuarios que aun no existen
    @Transactional
    public ActivationToken inviteUser(InvitationRequest request) {

        try {

            Long institutionId = resolveInstitutionId(request);
            validateRole(request.getRole());

            // buscar usuario global
            User user = userProvisioningService
                    .provisionUser(
                            UserProvisioningRequest.builder()
                                    .email(request.getEmail())
                                    .build()
                    );

            institutionMembershipService.createMembership(

                    CreateMembershipRequest.builder()
                            .userId(user.getId())
                            .institutionId(institutionId)
                            .role(request.getRole())
                            .active(false)
                            .build()
            );

            // invitación token
            ActivationToken token =
                    activationTokenService.createToken(
                            ActivationTokenRequest.builder()
                                    .userId(user.getId())
                                    .institutionId(institutionId)
                                    .invitedBy(UserContext.getUserId())
                                    .build()
                    );

            // enviar email
            invitationEmailService.inviteUser(
                    user.getEmail(),
                    token.getToken(),
                    false
            );

            return token;

        } catch (Exception e) {

            System.err.println("ERROR EN inviteUser()");
            System.err.println(e.getMessage());

            throw new RuntimeException("Error enviando invitación: " + e.getMessage(), e);
        }
    }

    // para usuario ya registrados
    @Transactional
    public ActivationToken createActivationInvitation(
            User user,
            Long institutionId) {

        ActivationToken token =
                activationTokenService.createToken(

                        ActivationTokenRequest.builder()
                                .userId(user.getId())
                                .institutionId(institutionId)
                                .invitedBy(UserContext.getUserId())
                                .build()
                );

        invitationEmailService.inviteUser(
                user.getEmail(),
                token.getToken(),
                false
        );

        return token;
    }

    @Transactional
    public List<InvitationResponse> getInvitations() {

        Long institutionId = getCurrentInstitutionId();

        List<ActivationToken> tokens =
                activationTokenRepository.findByInstitution_Id(institutionId);

        return tokens.stream()
                .map(this::mapInvitationResponse)
                .toList();
    }

    // reenviar invitación
    @Transactional
    public void resendInvitation(String email) {

        Long institutionId = getCurrentInstitutionId();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ActivationToken previousToken =
                activationTokenRepository
                        .findTopByUserIdAndInstitutionIdOrderByCreatedAtDesc(
                                user.getId(),
                                institutionId
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Invitation not found"));

        if (previousToken.isUsed()) {
            throw new RuntimeException("Invitation already used");
        }

        ActivationToken newToken =
                activationTokenService.createToken(
                        ActivationTokenRequest.builder()
                                .userId(user.getId())
                                .institutionId(institutionId)
                                .invitedBy(UserContext.getUserId())
                                .build()
                );

        invitationEmailService.inviteUser(
                user.getEmail(),
                newToken.getToken(),
                false
        );
    }

    private Long resolveInstitutionId(InvitationRequest request) {

        // SUPER ADMIN
        if (UserContext.getRole() == RoleName.SUPER_ADMIN) {

            if (request.getInstitutionId() == null) {
                throw new RuntimeException("InstitutionId is required");
            }

            validateInstitutionExists(request.getInstitutionId());

            return request.getInstitutionId();
        }

        // CONTEXTO INSTITUCIONAL
        Long institutionId = UserContext.getInstitutionId();

        if (institutionId == null) {
            throw new RuntimeException("No institution context");
        }

        validateInstitutionExists(institutionId);

        return institutionId;
    }

    private void validateInstitutionExists(Long institutionId) {

        boolean exists = institutionRepository.existsById(institutionId);

        if (!exists) {
            throw new RuntimeException("Institution not found");
        }
    }

    private void validateRole(RoleName role) {

        if (role == RoleName.SUPER_ADMIN) {
            throw new RuntimeException("Cannot invite SUPER_ADMIN");}
    }

    private InvitationResponse mapInvitationResponse(
            ActivationToken token) {

        // OBTENER ROL DESDE RELACIÓN INSTITUTION_USER
        InstitutionUser relation =
                institutionUserRepository
                        .findByUserIdAndInstitutionId(
                                token.getUser().getId(),
                                token.getInstitution().getId()
                        )
                        .orElse(null);

        InvitationResponse response = new InvitationResponse();

        response.setEmail(token.getUser().getEmail());

        response.setRole(
                relation != null
                        ? relation.getRole().getName().name()
                        : "PENDING"
        );

        response.setUsed(token.isUsed());
        response.setExpiresAt(token.getExpiresAt());

        return response;
    }

    private Long getCurrentInstitutionId() {

        Long institutionId = UserContext.getInstitutionId();
        if (institutionId == null) {
            throw new RuntimeException("No institution context");
        }
        return institutionId;
    }
}