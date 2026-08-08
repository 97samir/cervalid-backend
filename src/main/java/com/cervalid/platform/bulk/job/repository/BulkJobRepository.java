package com.cervalid.platform.bulk.job.repository;

import com.cervalid.platform.bulk.job.entity.BulkJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulkJobRepository
        extends JpaRepository<BulkJob, Long> {
}