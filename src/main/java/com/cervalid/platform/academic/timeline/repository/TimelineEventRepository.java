package com.cervalid.platform.academic.timeline.repository;

import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimelineEventRepository extends
        JpaRepository<TimelineEvent, Long>,
        JpaSpecificationExecutor<TimelineEvent> {

    List<TimelineEvent> findByStudentIdAndInstitutionIdOrderByEventDateDesc(
            Long studentId,
            Long institutionId);

    boolean existsByInstitutionIdAndStudentIdAndReferenceIdAndReferenceTypeAndType(
            Long institutionId,
            Long studentId,
            UUID referenceId,
            TimelineReferenceType referenceType,
            TimelineEventType type
    );

    Optional<TimelineEvent> findByPublicIdAndDeletedFalse(
            UUID publicId);

    List<TimelineEvent>
    findByStudentIdAndDeletedFalseOrderByEventDateDesc(
            Long studentId);

    Page<TimelineEvent> findByStudentIdAndInstitutionIdAndDeletedFalse(
            Long studentId,
            Long institutionId,
            Pageable pageable);

    List<TimelineEvent> findByStudentIdOrderByEventDateDesc(
            Long studentId);

    Page<TimelineEvent>
    findByStudentIdAndInstitutionIdAndDeletedFalseOrderByEventDateDesc(
            Long studentId,
            Long institutionId,
            Pageable pageable);

    long countByInstitutionId(Long institutionId);

    long countByInstitutionIdAndType(
            Long institutionId,
            TimelineEventType type);
}