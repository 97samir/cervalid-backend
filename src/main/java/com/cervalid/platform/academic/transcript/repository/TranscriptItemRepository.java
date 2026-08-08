package com.cervalid.platform.academic.transcript.repository;

import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TranscriptItemRepository
        extends JpaRepository<TranscriptItem, Long> {

    List<TranscriptItem> findByTranscriptId(
            Long transcriptId);

    boolean existsByTranscriptIdAndCourseCode(
            Long transcriptId,
            String courseCode);

    boolean existsByTranscriptIdAndCourseCodeAndIdNot(
            Long transcriptId,
            String courseCode,
            Long itemId
    );

    Optional<TranscriptItem> findByPublicId(
            UUID publicId);

    Optional<TranscriptItem> findByPublicIdAndTranscriptId(
            UUID publicId,
            Long transcriptId
    );
}
