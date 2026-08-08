package com.cervalid.platform.membership.service;

import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.membership.dto.CreateMembershipRequest;
import com.cervalid.platform.membership.dto.MembershipResponse;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.repository.RoleRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstitutionMembershipService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public MembershipResponse createMembership(CreateMembershipRequest request) {

        boolean exists = institutionUserRepository
                        .findByUserIdAndInstitutionId(
                                request.getUserId(),
                                request.getInstitutionId()
                        )
                        .isPresent();

        if (exists) {
            throw new RuntimeException("El usuario ya pertenece a esta institución");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Institution institution =
                institutionRepository.findById(request.getInstitutionId())
                        .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        Role role =
                roleRepository.findByName(request.getRole())
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        InstitutionUser relation = new InstitutionUser();

        relation.setUser(user);
        relation.setInstitution(institution);
        relation.setRole(role);
        relation.setActive(request.getActive() != null ?
                request.getActive() : false);

        InstitutionUser saved =
            institutionUserRepository.save(relation);
        return mapToResponse(saved);
    }

    public Long getInstitutionId(Long membershipId) {
        return institutionUserRepository
                .findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership not found"))
                .getInstitution()
                .getId();
    }

    // para actualizar estado en academic_student
    public InstitutionUser getMembership(
            Long userId,
            Long institutionId) {

        return institutionUserRepository
                .findByUserIdAndInstitutionId(
                        userId,
                        institutionId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Membership not found"));
    }

    // acepatr invitación, acticar cuenta
    @Transactional
    public void activateMembership(Long userId, Long institutionId) {

        InstitutionUser membership =
                institutionUserRepository
                        .findByUserIdAndInstitutionId(
                                userId,
                                institutionId
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Membership not found"));

        if (membership == null) {
            throw new RuntimeException("El usuario aún no tiene relación con la institución");
        }

        membership.setActive(true);

        institutionUserRepository.save(membership);
    }

    // institucion inhabilitada, expulsion, suspension, egreso, retiro
    @Transactional
    public void deactivateMembership(Long userId, Long institutionId) {

        InstitutionUser membership =
                institutionUserRepository
                        .findByUserIdAndInstitutionId(
                                userId,
                                institutionId
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Membership not found"));

        membership.setActive(false);

        institutionUserRepository.save(membership);
    }

    private MembershipResponse mapToResponse(
            InstitutionUser membership) {

        return MembershipResponse.builder()
                .institutionUserId(membership.getId())
                .userId(membership.getUser().getId())
                .institutionId(membership.getInstitution().getId())
                .role(membership.getRole().getName().toString())
                .active(membership.isActive())
                .build();
    }
}