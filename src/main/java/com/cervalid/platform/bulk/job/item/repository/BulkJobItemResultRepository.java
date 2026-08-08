package com.cervalid.platform.bulk.job.item.repository;

import com.cervalid.platform.bulk.job.item.entity.BulkJobItemResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BulkJobItemResultRepository
        extends JpaRepository<BulkJobItemResult, Long> {

    List<BulkJobItemResult> findByBulkJobId(Long bulkJobId);
}