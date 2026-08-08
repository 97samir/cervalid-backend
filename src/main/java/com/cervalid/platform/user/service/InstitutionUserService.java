package com.cervalid.platform.user.service;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstitutionUserService {

    private final InstitutionUserRepository repository;

    public void changeStatus(Long id, boolean active) {

        InstitutionUser relation = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relación no encontrada"));

        RoleName role = UserContext.getRole();
        Long institutionId = UserContext.getInstitutionId();

        // SUPER ADMIN puede todo
        if (role == RoleName.SUPER_ADMIN) {
            relation.setActive(active);
            repository.save(relation);
            return;
        }

        // ADMIN solo su institución
        if (role == RoleName.INSTITUTION_ADMIN) {

            if (!relation.getInstitution().getId().equals(institutionId)) {
                throw new RuntimeException("No puedes modificar usuarios de otra institución");
            }

            relation.setActive(active);
            repository.save(relation);
            return;
        }

        throw new RuntimeException("No tienes permisos");
    }
}
