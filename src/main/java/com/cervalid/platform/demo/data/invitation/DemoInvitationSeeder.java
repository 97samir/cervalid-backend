package com.cervalid.platform.demo.data.invitation;

import com.cervalid.platform.auth.token.dto.ActivationTokenRequest;
import com.cervalid.platform.auth.token.entity.ActivationToken;
import com.cervalid.platform.auth.token.service.ActivationTokenService;
import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import com.cervalid.platform.membership.service.InstitutionMembershipService;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.provisioning.dto.UserProvisioningRequest;
import com.cervalid.platform.user.provisioning.service.UserProvisioningService;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoInvitationSeeder {

    private static final String UNIVERSITY_RUC = "DEMO-CERV-001";
    private static final String INSTITUTE_RUC = "DEMO-CERV-002";

    private final InstitutionRepository institutionRepository;
    private final UserRepository userRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final UserProvisioningService userProvisioningService;
    private final InstitutionMembershipService institutionMembershipService;
    private final ActivationTokenService activationTokenService;

    private record DemoInvitation(String email, RoleName role) {}

    private static final List<DemoInvitation> DEMO_INVITATIONS = List.of(
            new DemoInvitation("demo.invitado.admin@cervalid.com",
                    RoleName.INSTITUTION_ADMIN),
            new DemoInvitation("demo.invitado.subadmin@cervalid.com",
                    RoleName.INSTITUTION_SUBADMIN),
            new DemoInvitation("demo.invitado.estudiante@cervalid.com",
                    RoleName.STUDENT),
            new DemoInvitation("demo.invitado.reclutador@cervalid.com",
                    RoleName.RECRUITER)
    );

    @Transactional
    public void seed() {
        seedInstitution(UNIVERSITY_RUC);
        seedInstitution(INSTITUTE_RUC);
    }

    private void seedInstitution(String ruc) {
        Institution institution = institutionRepository.findByRuc(ruc)
                .orElseThrow(() -> new IllegalStateException(
                        "No se encontró la institución demo: " + ruc));

        int created = 0;

        for (DemoInvitation invitation : DEMO_INVITATIONS) {
            if (seedInvitation(institution, invitation)) {
                created++;
            }
        }

        log.info(
                "DemoInvitationSeeder: {} invitaciones pendientes nuevas para institución {}",
                created,
                ruc
        );
    }

    private boolean seedInvitation(
            Institution institution,
            DemoInvitation invitation
    ) {
        User user = userRepository.findByEmail(invitation.email())
                .orElseGet(() -> userProvisioningService.provisionUser(
                        UserProvisioningRequest.builder()
                                .email(invitation.email())
                                .build()
                ));

        InstitutionUser membership = institutionUserRepository
                .findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                )
                .orElse(null);

        if (membership == null) {
            institutionMembershipService.createMembership(
                    CreateMembershipRequest.builder()
                            .userId(user.getId())
                            .institutionId(institution.getId())
                            .role(invitation.role())
                            .active(false)
                            .build()
            );
        } else if (membership.isActive()) {
            // No convertir una membresía activa en una invitación pendiente.
            return false;
        }

        if (activationTokenService.hasActiveToken(
                user.getId(),
                institution.getId()
        )) {
            return false;
        }

        ActivationToken token = activationTokenService.createToken(
                ActivationTokenRequest.builder()
                        .userId(user.getId())
                        .institutionId(institution.getId())
                        .build()
        );

        return token != null;
    }
}
