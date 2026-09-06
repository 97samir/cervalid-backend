package com.cervalid.platform.academic.transcript.repository;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TranscriptRepository
        extends JpaRepository<Transcript, Long> {

    Optional<Transcript> findByPublicId(
            UUID publicId);

    Optional<Transcript> findByIdAndInstitutionId(
            Long transcriptId,
            Long institutionId
    );

    List<Transcript> findByStudentId(
            Long studentId);

    List<Transcript> findByInstitutionId(
            Long institutionId);

    boolean existsByStudentIdAndAcademicPeriodTypeAndAcademicPeriod(
            Long studentId,
            AcademicPeriodType academicPeriodType,
            String academicPeriod);

    Optional<Transcript> findByPublicIdAndInstitutionId(
            UUID publicId,
            Long institutionId);

    List<Transcript> findByStudentIdAndInstitutionId(
            Long studentId,
            Long institutionId
    );
}
