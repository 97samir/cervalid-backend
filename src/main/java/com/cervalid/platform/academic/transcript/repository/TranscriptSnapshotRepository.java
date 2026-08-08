package com.cervalid.platform.academic.transcript.repository;

import com.cervalid.platform.academic.transcript.entity.TranscriptSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptSnapshotRepository
        extends JpaRepository<TranscriptSnapshot, Long> {
}