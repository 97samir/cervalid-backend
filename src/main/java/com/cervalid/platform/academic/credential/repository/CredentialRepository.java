package com.cervalid.platform.academic.credential.repository;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CredentialRepository
        extends JpaRepository<Credential, Long>,
        JpaSpecificationExecutor<Credential> {

    Optional<Credential> findByPublicIdAndInstitutionIdAndDeletedFalse(
            UUID publicId,
            Long institutionId
    );

    Optional<Credential> findByPublicIdAndDeletedFalse(
            UUID publicId
    );

    Page<Credential> findAllByInstitutionIdAndStudentIdAndDeletedFalse(
            Long institutionId,
            Long studentId,
            Pageable pageable
    );

    boolean existsByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
            Long institutionId,
            Long studentId,
            CredentialType type
    );

    boolean existsByInstitutionIdAndStudentIdAndTypeAndDeletedFalseAndIdNot(
            Long institutionId,
            Long studentId,
            CredentialType type,
            Long id
    );

    long countByInstitutionIdAndStatusAndDeletedFalse(
            Long institutionId,
            CredentialStatus status
    );

    Optional<Credential> findByInstitutionIdAndStudentIdAndTypeAndStatusAndDeletedFalse(
            Long institutionId,
            Long studentId,
            CredentialType type,
            CredentialStatus status
    );

}