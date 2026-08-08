package com.cervalid.platform.bulk.job.item.service;

import com.cervalid.platform.bulk.job.item.dto.response.BulkJobItemResultResponse;
import com.cervalid.platform.bulk.job.item.dto.response.BulkJobItemsResponse;
import com.cervalid.platform.bulk.job.item.repository.BulkJobItemResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BulkJobItemQueryService {

    private final BulkJobItemResultRepository bulkJobItemResultRepository;

    public BulkJobItemsResponse getItems(Long jobId) {

        List<BulkJobItemResultResponse> items =
                bulkJobItemResultRepository.findByBulkJobId(jobId)
                        .stream()
                        .map(item ->
                                BulkJobItemResultResponse.builder()
                                        .itemId(item.getId())
                                        .rowNumber(item.getRowNumber())
                                        .success(item.isSuccess())
                                        .errorMessage(item.getErrorMessage())
                                        .payload(item.getPayload())
                                        .build()
                        )
                        .toList();

        int successItems =
                (int) items.stream()
                        .filter(BulkJobItemResultResponse::isSuccess)
                        .count();

        int failedItems = items.size() - successItems;

        return BulkJobItemsResponse.builder()
                .jobId(jobId)
                .totalItems(items.size())
                .successItems(successItems)
                .failedItems(failedItems)
                .items(items)
                .build();
    }
}