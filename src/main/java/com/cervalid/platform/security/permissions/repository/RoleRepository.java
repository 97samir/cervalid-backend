package com.cervalid.platform.security.permissions.repository;

import com.cervalid.platform.common.enums.RoleName;
import com.cervalid.platform.security.permissions.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
