package com.cervalid.platform.bulk.job.item.service;

import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.item.entity.BulkJobItemResult;
import com.cervalid.platform.bulk.job.item.repository.BulkJobItemResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BulkJobItemResultService {

    private final BulkJobItemResultRepository repository;

    public void saveResult(
            BulkJob job,
            Integer rowNumber,
            String payload,
            boolean success,
            String errorMessage,
            String status) {

        repository.save(
                BulkJobItemResult.builder()
                        .bulkJob(job)
                        .rowNumber(rowNumber)
                        .payload(payload)
                        .success(success)
                        .errorMessage(errorMessage)
                        .status(status)
                        .retryCount(0)
                        .processedAt(LocalDateTime.now())
                        .build()
        );
    }
}