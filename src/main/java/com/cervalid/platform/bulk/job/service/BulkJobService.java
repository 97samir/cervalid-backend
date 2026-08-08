package com.cervalid.platform.bulk.job.service;

import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.enums.BulkJobStatus;
import com.cervalid.platform.bulk.job.repository.BulkJobRepository;
import com.cervalid.platform.security.context.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BulkJobService {

    private final BulkJobRepository repository;

    public BulkJob createJob(String jobType, Long institutionId) {

        return repository.save(
                BulkJob.builder()
                        .jobType(jobType)
                        .status(BulkJobStatus.PENDING)
                        .institutionId(institutionId)
                        .createdBy(UserContext.getUserId())
                        .createdAt(LocalDateTime.now())
                        .successCount(0)
                        .failedCount(0)
                        .totalRows(0)
                        .build()
        );
    }

    public void updateStatus(Long jobId, BulkJobStatus status) {

        BulkJob job = repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setStatus(status);

        if (status == BulkJobStatus.COMPLETED || status == BulkJobStatus.FAILED) {
            job.setFinishedAt(LocalDateTime.now());
        }

        repository.save(job);
    }

    public void updateCounters(Long jobId, int success, int failed) {

        BulkJob job = repository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        job.setSuccessCount(success);
        job.setFailedCount(failed);
        job.setTotalRows(success + failed);

        repository.save(job);
    }

    public BulkJob getJobEntity(Long jobId) {
        return repository.findById(jobId)
                .orElseThrow(() ->
                        new RuntimeException("BulkJob no encontrado: " + jobId)
                );
    }
}