package com.cervalid.platform.bulk.job.service;

import com.cervalid.platform.bulk.job.dto.BulkJobResponse;
import com.cervalid.platform.bulk.job.dto.BulkJobResultResponse;
import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.entity.BulkJobResult;
import com.cervalid.platform.bulk.job.mapper.BulkJobMapper;
import com.cervalid.platform.bulk.job.repository.BulkJobRepository;
import com.cervalid.platform.bulk.job.repository.BulkJobResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BulkJobQueryService {

    private final BulkJobRepository bulkJobRepository;
    private final BulkJobResultRepository bulkJobResultRepository;
    private final BulkJobMapper bulkJobMapper;

    public BulkJobResponse getJob(Long jobId) {

        BulkJob job = bulkJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return bulkJobMapper.toResponse(job);
    }

    public BulkJobResultResponse getResult(Long jobId) {

        BulkJob job = bulkJobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        BulkJobResult result = bulkJobResultRepository.findByJobId(jobId)
                .orElseThrow();

        return BulkJobResultResponse.builder()
                .jobId(job.getId())
                .jobType(job.getJobType())
                .status(job.getStatus().name())
                .successCount(job.getSuccessCount())
                .failedCount(job.getFailedCount())
                .totalRows(job.getTotalRows())
                .build();
    }
}