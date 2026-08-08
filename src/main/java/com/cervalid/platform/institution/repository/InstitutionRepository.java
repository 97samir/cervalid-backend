package com.cervalid.platform.institution.repository;

import com.cervalid.platform.institution.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstitutionRepository
        extends JpaRepository<Institution, Long> {

    List<Institution> findByActive(Boolean active);

    // para dashboard stats de superadmin
    long countByActiveTrue();

    //List<Institution> findByInactive(Boolean inactive);
    //List<Institution> findByAll(Boolean active);
}
