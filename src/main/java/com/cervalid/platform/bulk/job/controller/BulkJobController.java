package com.cervalid.platform.bulk.job.controller;

import com.cervalid.platform.bulk.job.dto.BulkJobCreateRequest;
import com.cervalid.platform.bulk.job.dto.BulkJobResponse;
import com.cervalid.platform.bulk.job.dto.BulkJobResultResponse;
import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.enums.BulkJobStatus;
import com.cervalid.platform.bulk.job.item.dto.response.BulkJobItemsResponse;
import com.cervalid.platform.bulk.job.item.service.BulkJobItemQueryService;
import com.cervalid.platform.bulk.job.mapper.BulkJobMapper;
import com.cervalid.platform.bulk.job.processor.BulkJobProcessor;
import com.cervalid.platform.bulk.job.service.BulkJobQueryService;
import com.cervalid.platform.bulk.job.service.BulkJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bulk/jobs")
@RequiredArgsConstructor
public class BulkJobController {

    private final BulkJobService jobService;
    private final BulkJobQueryService bulkJobQueryService;
    private final BulkJobProcessor processor;
    private final BulkJobMapper bulkJobMapper;
    private final BulkJobItemQueryService bulkJobItemQueryService;

    // inicia proceso masivo, no espera resultado
    @PostMapping("/invitations")
    public BulkJobResponse createJob(@RequestBody BulkJobCreateRequest request) {

        BulkJob job = jobService.createJob(
                request.getJobType(),
                1L);

        processor.processInvitationJob(
                job.getId(),
                request.getRows(),
                job.getInstitutionId()
        );

        return BulkJobResponse.builder()
                .jobId(job.getId())
                .jobType(job.getJobType())
                .status(BulkJobStatus.PENDING.name())
                .createdAt(job.getCreatedAt())
                .finishedAt(null)
                .build();
    }

    // devuelve estado general PENDING | PROCESSING | COMPLETED | FAILED
    @GetMapping("/{id}")
    public BulkJobResponse getJob(@PathVariable Long id) {
        return bulkJobQueryService.getJob(id);
    }

    // resumen rapido, cards de estadistica
    @GetMapping("/{id}/result")
    public BulkJobResultResponse getResult(@PathVariable Long id) {
        return bulkJobQueryService.getResult(id);
    }

    // detalle por fila, si es duplicado u otro
    @GetMapping("/{id}/items")
    public BulkJobItemsResponse getItems(
            @PathVariable Long id) {
        return bulkJobItemQueryService.getItems(id);
    }
}