package com.cervalid.platform.user.service;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.security.context.UserContext;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.repository.RoleRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.user.dto.*;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Obtener un usuario
    public User getUser(Long userId) {

        RoleName currentRole = UserContext.getRole();
        Long currentInstitutionId = UserContext.getInstitutionId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (currentRole == RoleName.SUPER_ADMIN) {
            return user;
        }

        if (currentInstitutionId == null) {
            throw new RuntimeException("Usuario sin institución");
        }

        institutionUserRepository
                .findByUserIdAndInstitutionId(userId, currentInstitutionId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no pertenece a tu institución"));

        return user;
    }

    //Crear usuario
    @Transactional
    public User createUser(CreateUserRequest request) {

        RoleName currentRole = UserContext.getRole();
        Long currentInstitutionId = UserContext.getInstitutionId();

        Institution institution;

        // SUPER_ADMIN puede elegir institución destino
        if (currentRole == RoleName.SUPER_ADMIN) {

            if (request.getInstitutionId() == null) {
                throw new RuntimeException("Debe especificar institution");
            }

            institution = institutionRepository
                    .findById(request.getInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        } else {
            // ADMIN institucional solo su tenant
            if (currentInstitutionId == null) {
                throw new RuntimeException("Usuario sin institución");
            }

            institution = institutionRepository
                    .findById(currentInstitutionId)
                    .orElseThrow(() -> new RuntimeException("Institución no encontrada"));
        }
        // validar rol solicitado
        Role role = roleRepository
                .findByName(RoleName.valueOf(request.getRole().toString()))
                .orElseThrow(() -> new RuntimeException("Rol inválido"));

        // buscar usuario si existe globalmente
        Optional<User> existingUser =
                userRepository.findByEmailIgnoreCase(
                        request.getEmail().trim());

        if (existingUser.isPresent()) {

            User user = existingUser.get();

            boolean alreadyBelongs =
                    institutionUserRepository
                            .findByUserIdAndInstitutionId(
                                    user.getId(),
                                    institution.getId())
                            .isPresent();

            if (alreadyBelongs) {
                throw new RuntimeException(
                        "El usuario ya pertenece a esta institución");
            }

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este correo ya existe en Cervalid. ¿Desea vincular a su institución?"
            );
        }

        User user = new User();

        user.setName(request.getName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().trim());
        user.setDocumentType(request.getDocumentType());
        user.setDocument(request.getDocument());
        user.setPhone(request.getPhone());
        user.setActive(false);
        user.setPassword(passwordEncoder.encode(
                UUID.randomUUID().toString())
        );

        user = userRepository.save(user);

        // crear relacion
        InstitutionUser relation = new InstitutionUser();
        relation.setUser(user);
        relation.setInstitution(institution);
        relation.setRole(role);
        relation.setActive(false);

        institutionUserRepository.save(relation);

        return user;
    }

    // activar usuario existente en la plataforma
    public void linkInstitution(LinkInstitutionRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Institution institution = institutionRepository
                .findById(request.getInstitutionId())
                .orElseThrow(() -> new RuntimeException("Institución no encontrada"));

        Optional<InstitutionUser> existing =
                institutionUserRepository.findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                );

        if (existing.isPresent()) {
            throw new RuntimeException("El usuario ya pertenece a esta institución");
        }

        InstitutionUser relation = new InstitutionUser();

        relation.setUser(user);
        relation.setInstitution(institution);

        Role role = roleRepository
                .findByName(RoleName.valueOf(request.getRole()))
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        relation.setRole(role);
        relation.setActive(true);

        institutionUserRepository.save(relation);
    }

    // eliminar usuario
    public void deleteUser(Long userIdToDelete) {

        try {

            RoleName currentRole = UserContext.getRole();
            Long currentInstitutionId = UserContext.getInstitutionId();

            User user = userRepository.findById(userIdToDelete)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Long targetInstitutionId;

            if (currentRole == RoleName.SUPER_ADMIN) {

                List<InstitutionUser> relations =
                        institutionUserRepository.findAllByUserId(user.getId());

                if (relations.isEmpty()) {
                    throw new RuntimeException("El usuario no pertenece a ninguna institución");
                }

                for (InstitutionUser relation : relations) {
                    if (relation.getRole().getName() == RoleName.SUPER_ADMIN) {
                        throw new RuntimeException("No se puede eliminar un SUPER_ADMIN");
                    }
                }

                institutionUserRepository.deleteAll(relations);
                userRepository.delete(user);
                return;

            } else {

                if (currentInstitutionId == null) {
                    throw new RuntimeException("Usuario sin institución");
                }

                targetInstitutionId = currentInstitutionId;

                InstitutionUser relation =
                        institutionUserRepository
                                .findByUserIdAndInstitutionId(userIdToDelete, targetInstitutionId)
                                .orElseThrow(() ->
                                        new RuntimeException("No pertenece a tu institución"));

                if (relation.getRole().getName() == RoleName.SUPER_ADMIN) {
                    throw new RuntimeException("No se puede eliminar un SUPER_ADMIN");
                }

                institutionUserRepository.delete(relation);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando usuario: " + e.getMessage());
        }
    }

    // CAMBIAR ROL DE USUARIO PATCH /users/{id}/role
    public void updateUserRole(Long userId, UpdateUserRoleRequest request) {

        RoleName currentRole = UserContext.getRole();
        Long currentInstitutionId = UserContext.getInstitutionId();

        Long targetInstitutionId;
        // SUPER_ADMIN usa la institución del request
        if (currentRole == RoleName.SUPER_ADMIN) {

            if (request.getInstitutionId() == null) {
                throw  new RuntimeException("Debes especificar institución");
            }
            targetInstitutionId = request.getInstitutionId();

        } else {
            if (currentInstitutionId == null) {
                throw new RuntimeException("Usuario sin institución");
            }
            targetInstitutionId = currentInstitutionId;
        }

        InstitutionUser relation =
                institutionUserRepository
                .findByUserIdAndInstitutionId(userId, targetInstitutionId)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no pertenece a tu institución"));

        // nadie puede modificar SUPER_ADMIN excepto él mismo
        if (relation.getRole().getName() == RoleName.SUPER_ADMIN &&
                currentRole != RoleName.SUPER_ADMIN) {
            throw new RuntimeException("No puedes modificar un SUPER_ADMIN");
        }

        if (currentRole != RoleName.SUPER_ADMIN &&
                currentRole != RoleName.INSTITUTION_ADMIN) {
            throw new RuntimeException("No tienes permisos para cambiar roles");
        }

        if (request.getRole() == RoleName.SUPER_ADMIN) {
            throw new RuntimeException("No se puede asignar rol SUPER_ADMIN");
        }

        /*Role newRole = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Rol inválido"));
        */
        Role newRole = roleRepository
                .findByName(RoleName.valueOf(request.getRole().toString()))
                .orElseThrow(() -> new RuntimeException("Rol inválido"));

        relation.setRole(newRole);

        institutionUserRepository.save(relation);
    }

    // cambiar datos comunes con PATCH /users/{id}
    public User updateUser(Long userId, UpdateUserRequest request) {

        try {

            RoleName currentRole = UserContext.getRole();
            Long currentInstitutionId = UserContext.getInstitutionId();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Long targetInstitutionId;

            if (currentRole == RoleName.SUPER_ADMIN) {

                if (request.getInstitutionId() == null) {
                    throw new RuntimeException("Debes especificar institución");
                }

                targetInstitutionId = request.getInstitutionId();

            } else {

                if (currentInstitutionId == null) {
                    throw new RuntimeException("Usuario sin institución");
                }

                targetInstitutionId = currentInstitutionId;
            }

            InstitutionUser relation =
                    institutionUserRepository
                            .findByUserIdAndInstitutionId(userId, targetInstitutionId)
                            .orElseThrow(() ->
                                    new RuntimeException("Usuario no pertenece a tu institución"));

            if (relation.getRole().getName() == RoleName.SUPER_ADMIN &&
                    currentRole != RoleName.SUPER_ADMIN) {
                throw new RuntimeException("No puedes modificar un SUPER_ADMIN");
            }

            if (currentRole == RoleName.INSTITUTION_ADMIN &&
                    relation.getRole().getName() == RoleName.INSTITUTION_ADMIN) {
                throw new RuntimeException("No puedes modificar a otro administrador");
            }

            if (request.getName() != null) {
                user.setName(request.getName());
            }

            if (request.getLastName() != null) {
                user.setLastName(request.getLastName());
            }

            if (request.getEmail() != null) {
                user.setEmail(request.getEmail());
            }
            /*
            if (request.getDocument() != null) {
                user.setDocument(request.getDocument());
            }
            */
            if (request.getPhone() != null) {
                user.setPhone(request.getPhone());
            }

            if (request.getActive() != null) {
                user.setActive(request.getActive());
            }

            userRepository.save(user);

            return user;

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando usuario: " + e.getMessage());
        }
    }

    // metodo page para listar usuarios
    public Page<UserResponse> listUsers(UserFilterRequest filter, Pageable pageable) {

        try {

            RoleName currentRole = UserContext.getRole();
            Long currentInstitutionId = UserContext.getInstitutionId();

            Long institutionIdToUse;

            // ===== CONTEXTO =====
            if (currentRole == RoleName.SUPER_ADMIN) {
                institutionIdToUse = filter.getInstitutionId(); // viene del frontend
            } else {
                if (currentInstitutionId == null) {
                    throw new RuntimeException("Usuario sin institución");
                }
                institutionIdToUse = currentInstitutionId;
            }

            Page<InstitutionUser> relations;

            // ===== CASOS =====

            // FILTRAR POR INSTITUCIÓN
            if (institutionIdToUse != null) {

                if (filter.getRole() != null && filter.getActive() != null) {
                    relations = institutionUserRepository
                            .findAllByInstitution_IdAndRole_NameAndActive(
                                    institutionIdToUse,
                                    filter.getRole(),
                                    filter.getActive(),
                                    pageable
                            );

                } else if (filter.getRole() != null) {
                    relations = institutionUserRepository
                            .findAllByInstitution_IdAndRole_Name(
                                    institutionIdToUse,
                                    filter.getRole(),
                                    pageable
                            );

                } else if (filter.getActive() != null) {
                    relations = institutionUserRepository
                            .findAllByInstitution_IdAndActive(
                                    institutionIdToUse,
                                    filter.getActive(),
                                    pageable
                            );

                } else {
                    relations = institutionUserRepository
                            .findAllByInstitution_Id(
                                    institutionIdToUse,
                                    pageable
                            );
                }

            }

            // GLOBAL SUPER_ADMIN sin filtro
            else {

                if (filter.getRole() != null && filter.getActive() != null) {
                    relations = institutionUserRepository
                            .findAllByRole_NameAndActive(
                                    filter.getRole(),
                                    filter.getActive(),
                                    pageable
                            );

                } else if (filter.getRole() != null) {
                    relations = institutionUserRepository
                            .findAllByRole_Name(
                                    filter.getRole(),
                                    pageable
                            );

                } else if (filter.getActive() != null) {
                    relations = institutionUserRepository
                            .findAllByActive(
                                    filter.getActive(),
                                    pageable
                            );

                } else {
                    relations = institutionUserRepository.findAll(pageable);
                }
            }

            return relations.map(this::mapToUserResponse);

        } catch (Exception e) {

            System.err.println("ERROR EN listUsers()");
            System.err.println(e.getMessage());

            throw new RuntimeException("Error listando usuarios: " + e.getMessage(), e);
        }
    }

    // metodo para listar usuario de una institución en especifico
    public Page<UserResponse> listUsersByInstitution(UserFilterRequest filter, Pageable pageable) {

        Long institutionId = UserContext.getInstitutionId();

        if (institutionId == null) {
            throw new RuntimeException("No estás en contexto de institución");
        }

        Page<InstitutionUser> relations;

        if (filter.getRole() != null && filter.getActive() != null) {

            relations = institutionUserRepository
                    .findAllByInstitution_IdAndRole_NameAndActive(
                            institutionId,
                            filter.getRole(),
                            filter.getActive(),
                            pageable
                    );

        } else if (filter.getRole() != null) {

            relations = institutionUserRepository
                    .findAllByInstitution_IdAndRole_Name(
                            institutionId,
                            filter.getRole(),
                            pageable
                    );

        } else if (filter.getActive() != null) {

            relations = institutionUserRepository
                    .findAllByInstitution_IdAndActive(
                            institutionId,
                            filter.getActive(),
                            pageable
                    );

        } else {

            relations = institutionUserRepository
                    .findAllByInstitution_Id(
                            institutionId,
                            pageable
                    );
        }

        return relations.map(this::mapToUserResponse);
    }

    // listar usuarios para el frontend superadmin
    public Page<UserResponse> listAllUsers(UserFilterRequest filter, Pageable pageable) {

        Page<InstitutionUser> relations;

        Long institutionId = filter.getInstitutionId();

        // FILTRAR POR INSTITUCIÓN
        if (institutionId != null) {

            if (filter.getRole() != null && filter.getActive() != null) {

                relations = institutionUserRepository
                        .findAllByInstitution_IdAndRole_NameAndActive(
                                institutionId,
                                filter.getRole(),
                                filter.getActive(),
                                pageable
                        );

            } else if (filter.getRole() != null) {

                relations = institutionUserRepository
                        .findAllByInstitution_IdAndRole_Name(
                                institutionId,
                                filter.getRole(),
                                pageable
                        );

            } else if (filter.getActive() != null) {

                relations = institutionUserRepository
                        .findAllByInstitution_IdAndActive(
                                institutionId,
                                filter.getActive(),
                                pageable
                        );

            } else {

                relations = institutionUserRepository
                        .findAllByInstitution_Id(
                                institutionId,
                                pageable
                        );
            }

        }

        // GLOBAL (SIN INSTITUCIÓN)
        else {

            if (filter.getRole() != null && filter.getActive() != null) {

                relations = institutionUserRepository
                        .findAllByRole_NameAndActive(
                                filter.getRole(),
                                filter.getActive(),
                                pageable
                        );

            } else if (filter.getRole() != null) {

                relations = institutionUserRepository
                        .findAllByRole_Name(
                                filter.getRole(),
                                pageable
                        );

            } else if (filter.getActive() != null) {

                relations = institutionUserRepository
                        .findAllByActive(
                                filter.getActive(),
                                pageable
                        );

            } else {

                relations = institutionUserRepository.findAll(pageable);
            }
        }

        return relations.map(this::mapToUserResponse);
    }

    // metodo reutilizable
    private UserResponse mapToUserResponse(InstitutionUser relation) {

        User user = relation.getUser();

        return UserResponse.builder()
                .id(user.getId())
                .institutionUserId(relation.getId()) // agregado
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .documentType(user.getDocumentType())
                .document(user.getDocument())
                .phone(user.getPhone())
                .active(relation.isActive())
                //.active(user.isActive())
                .role(relation.getRole().getName())
                .institutionId(relation.getInstitution().getId())
                .build();
    }
}
