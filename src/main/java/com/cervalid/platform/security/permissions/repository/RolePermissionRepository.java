package com.cervalid.platform.security.permissions.repository;

import com.cervalid.platform.security.permissions.Permission;
import com.cervalid.platform.security.permissions.Role;
import com.cervalid.platform.security.permissions.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(Role role);
    // para verificar si hay un dato existente
    boolean existsByRoleAndPermission(Role role, Permission permission);
}

