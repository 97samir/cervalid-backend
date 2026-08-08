package com.cervalid.platform.common.seed;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.common.validation.DocumentType;
import com.cervalid.platform.security.permissions.Permission;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.RolePermission;
import com.cervalid.platform.security.permissions.repository.PermissionRepository;
import com.cervalid.platform.security.permissions.repository.RolePermissionRepository;
import com.cervalid.platform.security.permissions.repository.RoleRepository;

import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    //inyectar
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    public void run(String... args) {
        seedRoles();
        seedPermissions();
        seedRolePermissions();
        seedUsersAndInstitutions();
    }
        // ---------------- ROLES ----------------
        private void seedRoles() {
            for (RoleName roleName : RoleName.values()) {
                roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(createRole(roleName)));
            }
        }

        private Role createRole(RoleName name) {
            Role role = new Role();
            role.setName(name);
            return role;
        }

        // ---------------- PERMISSIONS ----------------
        private void seedPermissions() {
            createPermission("CREATE_USER", "Crear usuarios");
            createPermission("EDIT_USER", "Editar usuarios");
            createPermission("VIEW_USERS", "Ver usuarios");
            createPermission("DELETE_USER", "Eliminar usuarios");
        }

        private void createPermission(String code, String description) {
            permissionRepository.findByCode(code)
                    .orElseGet(() -> {
                        Permission p = new Permission();
                        p.setCode(code);
                        p.setDescription(description);
                        return permissionRepository.save(p);
                    });
        }
    // ---------------- ROLE → PERMISSIONS ----------------
    private void seedRolePermissions() {
        assignPermissions(RoleName.SUPER_ADMIN,
                "CREATE_USER", "EDIT_USER", "VIEW_USERS", "DELETE_USER");
        assignPermissions(RoleName.INSTITUTION_ADMIN,
                "CREATE_USER", "EDIT_USER", "VIEW_USERS", "DELETE_USER");
        assignPermissions(RoleName.INSTITUTION_SUBADMIN,
                "CREATE_USER", "VIEW_USERS");
        assignPermissions(RoleName.INSTITUTION_AUDITOR,
                "VIEW_USERS");
    }

    private void assignPermissions(RoleName roleName, String... permissions) {

        Role role = roleRepository.findByName(roleName).orElseThrow();

        for (String code : permissions) {
            Permission permission =
                    permissionRepository.findByCode(code).orElseThrow();

            boolean exists = rolePermissionRepository
                    .existsByRoleAndPermission(role, permission);
            if (!exists) {
                RolePermission rp = new RolePermission();
                rp.setRole(role);
                rp.setPermission(permission);
                rolePermissionRepository.save(rp);
            }
        }
    }
    // ---------------- USERS + INSTITUTIONS ----------------
    private void seedUsersAndInstitutions() {
        // SUPER ADMIN
        userRepository.findByEmail(superAdminEmail)
                .orElseGet(this::createSuperAdmin);
    }

    @Value("${app.superadmin.email}")
    private String superAdminEmail;

    @Value("${app.superadmin.password}")
    private String superAdminPassword;

    private User createSuperAdmin() {
        User user = new User();
        user.setName("SuperAdmin");
        user.setLastName("Cervalid");
        user.setEmail(superAdminEmail);
        user.setPassword(passwordEncoder.encode(superAdminPassword));
        user.setDocumentType(DocumentType.DNI);
        user.setDocument("12345678");
        user.setPhone("123456789");
        user.setGlobalRole(RoleName.SUPER_ADMIN);
        user.setActive(true);
        return userRepository.save(user);
    }
}