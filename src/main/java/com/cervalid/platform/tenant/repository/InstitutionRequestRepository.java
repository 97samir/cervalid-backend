package com.cervalid.platform.tenant.repository;

import com.cervalid.platform.tenant.entity.InstitutionRequest;
import com.cervalid.platform.tenant.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InstitutionRequestRepository
        extends JpaRepository<InstitutionRequest, Long> {

    Optional<InstitutionRequest> findByInstitutionalEmail(String email);

    Optional<InstitutionRequest> findByRuc(String ruc);

    Optional<InstitutionRequest> findByInstitutionName(String institutionName);

    List<InstitutionRequest> findByStatus(RequestStatus status);

    // para dashboard stats de superadmin
    long countByStatus(RequestStatus status);
}
