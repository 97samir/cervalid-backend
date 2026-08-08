package com.cervalid.platform.bulk.job.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkJobResultResponse {

    private Long jobId;
    private String jobType;
    private String status;

    private int totalRows;
    private int successCount;
    private int failedCount;
}