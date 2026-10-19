package com.cervalid.platform.auth.mapper;

import com.cervalid.platform.auth.dto.response.InstitutionOption;
import com.cervalid.platform.auth.dto.response.MeResponse;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthMapper {

    public InstitutionOption toInstitutionOption(
            InstitutionUser relation
    ) {
        InstitutionOption option = new InstitutionOption();

        option.setInstitutionUserId(relation.getId());
        option.setInstitutionId(relation.getInstitution().getId());
        option.setInstitutionName(relation.getInstitution().getName());
        option.setRole(relation.getRole().getName().name());

        return option;
    }

    public List<InstitutionOption> toInstitutionOptions(
            List<InstitutionUser> relations
    ) {
        return relations.stream()
                .map(this::toInstitutionOption)
                .toList();
    }

    public MeResponse toMeResponse(
            User user,
            String role,
            Long institutionId,
            Long institutionUserId,
            String institutionName
    ) {
        MeResponse response = new MeResponse();

        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setLastName(user.getLastName());
        response.setDocument(user.getDocument());
        response.setPhone(user.getPhone());

        response.setRole(role);
        response.setInstitutionId(institutionId);
        response.setInstitutionUserId(institutionUserId);
        response.setInstitutionName(institutionName);

        return response;
    }
}
