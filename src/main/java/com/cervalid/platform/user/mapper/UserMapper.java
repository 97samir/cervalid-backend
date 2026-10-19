package com.cervalid.platform.user.mapper;

import com.cervalid.platform.user.dto.request.CreateUserRequest;
import com.cervalid.platform.user.dto.request.UpdateUserRequest;
import com.cervalid.platform.user.dto.response.UserResponse;
import com.cervalid.platform.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .publicId(user.getPublicId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .documentType(user.getDocumentType())
                .document(user.getDocument())
                .phone(user.getPhone())
                .active(user.isActive())
                .build();
    }

    // creacion de user
    public User toEntity(CreateUserRequest request) {
        if (request == null) {
            return null;
        }

        User user = new User();

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().trim());
        user.setDocumentType(request.getDocumentType());
        user.setDocument(request.getDocument());
        user.setPhone(request.getPhone());

        return user;
    }

    // actualización de user
    public void updateEntity(
            User user,
            UpdateUserRequest request) {

        if (user == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            user.setName(request.getName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail().trim());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        // Se mantiene deshabilitada la actualización de document,
        // tal como está actualmente en tu servicio.
    }

}