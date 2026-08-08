package com.cervalid.platform.user.repository;

import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    // búsqueda sin importar mayúsculas/minúsculas
    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByDocument(String document);

    // para dashboard stats de superadmin
    long count();
}

