package com.cervalid.platform.bulk.job.item.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BulkJobItemsResponse {

    private Long jobId;
    private Integer totalItems;
    private Integer successItems;
    private Integer failedItems;
    private List<BulkJobItemResultResponse> items;
}