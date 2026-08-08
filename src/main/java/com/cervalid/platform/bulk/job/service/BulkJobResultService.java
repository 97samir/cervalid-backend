package com.cervalid.platform.bulk.job.service;

import com.cervalid.platform.bulk.job.entity.BulkJobResult;
import com.cervalid.platform.bulk.job.repository.BulkJobResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BulkJobResultService {

    private final BulkJobResultRepository repository;
    private final ObjectMapper objectMapper;

    public void save(Long jobId, Object result) {

        try {
            BulkJobResult entity = BulkJobResult.builder()
                    .jobId(jobId)
                    .jsonResult(objectMapper.writeValueAsString(result))
                    .build();

            repository.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Error saving bulk result", e);
        }
    }
}
