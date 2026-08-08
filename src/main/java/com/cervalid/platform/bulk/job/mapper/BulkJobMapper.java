package com.cervalid.platform.bulk.job.mapper;

import com.cervalid.platform.bulk.job.dto.BulkJobResponse;
import com.cervalid.platform.bulk.job.entity.BulkJob;
import org.springframework.stereotype.Component;

@Component
public class BulkJobMapper {

    public BulkJobResponse toResponse(BulkJob job) {

        return BulkJobResponse.builder()
                .jobId(job.getId())
                .jobType(job.getJobType())
                .status(job.getStatus().name())
                .createdAt(job.getCreatedAt())
                .finishedAt(job.getFinishedAt())
                .build();
    }
}