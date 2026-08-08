package com.cervalid.platform.bulk.job.repository;

import com.cervalid.platform.bulk.job.entity.BulkJobResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BulkJobResultRepository
        extends JpaRepository<BulkJobResult, Long> {

    Optional<BulkJobResult> findByJobId(Long jobId);
}