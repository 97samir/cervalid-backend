package com.cervalid.platform.academic.competency.repository;

import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompetencyRepository
        extends JpaRepository<Competency, Long>,
        JpaSpecificationExecutor<Competency> {

    Optional<Competency> findByPublicIdAndInstitutionId(
            UUID publicId,
            Long institutionId);

    List<Competency> findByStudentIdAndInstitutionId(
            Long studentId,
            Long institutionId);

    boolean existsByStudentIdAndNameAndLevel(
            Long studentId,
            String name,
            CompetencyLevel level);

    boolean existsByStudentIdAndNameAndLevelAndIdNot(
            Long studentId,
            String name,
            CompetencyLevel level,
            Long id);

    Page<Competency> findAll(
            Specification<Competency> specification,
            Pageable pageable);
}