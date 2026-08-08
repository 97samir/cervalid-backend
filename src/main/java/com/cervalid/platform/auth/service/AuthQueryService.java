package com.cervalid.platform.auth.service;

import com.cervalid.platform.auth.dto.InstitutionOption;
import com.cervalid.platform.auth.dto.MeResponse;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthQueryService {

    private final UserRepository userRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final InstitutionRepository institutionRepository;

    public MeResponse getCurrentUser(String email){

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MeResponse response = new MeResponse();

        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setLastName(user.getLastName());
        response.setDocument(user.getDocument());
        response.setPhone(user.getPhone());

        response.setRole(
                UserContext.getRole() != null ?
                        UserContext.getRole().name() : null
        );
        Long institutionId = UserContext.getInstitutionId();

        response.setInstitutionId(institutionId);
        response.setInstitutionUserId(UserContext.getInstitutionUserId());

        // obtener nombre de institución desde la tabla Institution
        if (institutionId != null) {
            institutionRepository.findById(institutionId)
                    .ifPresent(inst -> response.setInstitutionName(inst.getName()));
        }

        System.out.println("ROLE en getCurrentUser: {}" + UserContext.getRole());

        return response;
    }

    public List<InstitutionOption> getMyInstitutions(String email){

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<InstitutionUser> relations =
                institutionUserRepository.findByUserIdAndActiveTrue(user.getId());

        return relations.stream().map(rel -> {

            InstitutionOption option = new InstitutionOption();

            option.setInstitutionUserId(rel.getId());
            option.setInstitutionId(rel.getInstitution().getId());
            option.setInstitutionName(rel.getInstitution().getName());
            option.setRole(rel.getRole().getName().toString());

            return option;

        }).toList();
    }

}