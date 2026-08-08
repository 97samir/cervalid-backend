package com.cervalid.platform.bulk.job.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BulkJobResponse {

    private Long jobId;
    private String jobType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime finishedAt;
}