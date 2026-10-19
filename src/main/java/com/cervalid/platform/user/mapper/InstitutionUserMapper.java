package com.cervalid.platform.user.mapper;

import com.cervalid.platform.user.dto.response.UserResponse;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class InstitutionUserMapper {

    public UserResponse toUserResponse(InstitutionUser relation) {

        if (relation == null) {
            return null;
        }

        User user = relation.getUser();

        return UserResponse.builder()
                .publicId(user.getPublicId())
                .institutionUserPublicId(relation.getPublicId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .documentType(user.getDocumentType())
                .document(user.getDocument())
                .phone(user.getPhone())
                .active(relation.isActive())
                .role(relation.getRole().getName())
                .institutionId(relation.getInstitution().getId())
                .build();
    }
}