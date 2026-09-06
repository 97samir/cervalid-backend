package com.cervalid.platform.academic.profile.repository;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AcademicProfileRepository
        extends JpaRepository<AcademicProfile, Long>,
        JpaSpecificationExecutor<AcademicProfile> {

    Optional<AcademicProfile> findByStudentId(
            Long studentId);

    Optional<AcademicProfile> findByStudentIdAndInstitutionId(
            Long studentId,
            Long institutionId
    );

    boolean existsByStudentId(
            Long studentId);

    Optional<AcademicProfile> findByPublicId
            (UUID publicId);

    Optional<AcademicProfile> findByPublicIdAndInstitutionId(
            UUID publicId,
            Long institutionId
    );

    List<AcademicProfile> findAllByStudentIdIn(
            Collection<Long> studentIds);

    Page<AcademicProfile> findByInstitutionId(
            Long institutionId,
            Pageable pageable
    );

}