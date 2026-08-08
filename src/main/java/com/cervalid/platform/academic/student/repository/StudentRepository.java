package com.cervalid.platform.academic.student.repository;

import com.cervalid.platform.academic.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends
        JpaRepository<Student, Long>,
        JpaSpecificationExecutor<Student> {

    Optional<Student> findByPublicId(
            UUID publicId);

    Optional<Student> findByInstitutionMembershipId(
            Long membershipId);

    boolean existsByInstitutionIdAndStudentCode(
            Long institutionId,
            String studentCode);

    Optional<Student> findByPublicIdAndInstitutionId(
            UUID studentPublicId,
            Long institutionId
    );
}