package com.cervalid.platform.academic.student.repository;

import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentInstitutionRepository
        extends JpaRepository<AcademicProfile, Long> {

    Optional<AcademicProfile> findByStudentIdAndInstitutionId(
            Long studentId, Long institutionId);

}