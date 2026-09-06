package com.cervalid.platform.academic.timeline.repository;

import com.cervalid.platform.academic.timeline.entity.TimelineEvent;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimelineEventRepository
        extends JpaRepository<TimelineEvent, Long>,
        JpaSpecificationExecutor<TimelineEvent> {

    boolean existsByInstitutionIdAndStudentIdAndReferenceIdAndReferenceTypeAndType(
            Long institutionId,
            Long studentId,
            UUID referenceId,
            TimelineReferenceType referenceType,
            TimelineEventType type
    );

    Optional<TimelineEvent> findByPublicIdAndDeletedFalse(
            UUID publicId
    );

    long countByInstitutionIdAndStudentIdAndDeletedFalse(
            Long institutionId,
            Long studentId
    );

    long countByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
            Long institutionId,
            Long studentId,
            TimelineEventType type
    );

    List<TimelineEvent> findByInstitutionIdAndStudentIdAndDeletedFalseOrderByEventDateDesc(
            Long institutionId,
            Long studentId
    );
}