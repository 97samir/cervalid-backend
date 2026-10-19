package com.cervalid.platform.institution.repository;

import com.cervalid.platform.institution.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository
        extends JpaRepository<Institution, Long> {

    List<Institution> findByActive(Boolean active);

    // para dashboard stats de superadmin
    long countByActiveTrue();

    boolean existsByRuc(String ruc);

    Optional<Institution> findByRuc(String ruc);
}
