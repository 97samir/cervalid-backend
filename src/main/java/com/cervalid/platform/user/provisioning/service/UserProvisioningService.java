package com.cervalid.platform.user.provisioning.service;

import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.user.provisioning.dto.UserProvisioningRequest;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User provisionUser(UserProvisioningRequest request) {

        return userRepository.findByEmail(request.getEmail())
                .orElseGet(() -> {

                    User user = new User();

                    user.setEmail(request.getEmail());
                    user.setName(request.getName() != null ? request.getName() : "PENDIENTE");
                    user.setLastName(request.getLastName() != null ? request.getLastName() : "PENDIENTE");
                    user.setDocumentType(request.getDocumentType() != null ? request.getDocumentType() : DocumentType.DNI);
                    user.setDocument(request.getDocument() != null ? request.getDocument() : UUID.randomUUID().toString().substring(0, 9));
                    user.setPhone(request.getPhone() != null ? request.getPhone() : "000000000");
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setActive(false);

                    return userRepository.save(user);
                });
    }
}