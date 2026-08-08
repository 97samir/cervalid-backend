package com.cervalid.platform.bulk.job.item.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BulkJobItemResultResponse {

    private Long itemId;
    private Integer rowNumber;
    private boolean success;
    private String errorMessage;
    private String payload;
}