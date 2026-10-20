package com.cervalid.platform.demo.data.user;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
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
public class DemoMembershipSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void seed() {
        // Universidad: cinco usuarios exclusivos y uno compartido.
        add("demo.admin@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.INSTITUTION_ADMIN);

        add("demo.subadmin@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.INSTITUTION_SUBADMIN);

        add("demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.STUDENT);

        add("demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.STUDENT);

        add("demo.recruiter@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.RECRUITER);

        add("demo.multiple@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                RoleName.INSTITUTION_AUDITOR);

        // Instituto: cinco usuarios exclusivos y el mismo usuario compartido.
        add("demo.instituto.admin@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.INSTITUTION_ADMIN);

        add("demo.instituto.subadmin@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.INSTITUTION_SUBADMIN);

        add("demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.STUDENT);

        add("demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.STUDENT);

        add("demo.instituto.auditor@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.INSTITUTION_AUDITOR);

        add("demo.multiple@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                RoleName.RECRUITER);
    }

    private void add(
            String email,
            String institutionRuc,
            RoleName roleName
    ) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el usuario demo: " + email
                ));

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe la institución demo: " + institutionRuc
                ));

        if (membershipRepository.findByUserIdAndInstitutionId(
                user.getId(), institution.getId()).isPresent()) {
            return;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el rol: " + roleName
                                + ". Verifica la inicialización de roles."
                ));

        InstitutionUser membership = new InstitutionUser();
        membership.setUser(user);
        membership.setInstitution(institution);
        membership.setRole(role);
        membership.setActive(true);

        membershipRepository.save(membership);
    }
}