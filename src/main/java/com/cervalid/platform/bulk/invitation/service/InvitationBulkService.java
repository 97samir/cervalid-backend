package com.cervalid.platform.bulk.invitation.service;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRequest;
import com.cervalid.platform.bulk.job.dto.BulkJobResponse;
import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.processor.BulkJobProcessor;
import com.cervalid.platform.bulk.job.service.BulkJobService;
import com.cervalid.platform.security.context.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationBulkService {

    private final BulkJobService jobService;
    private final BulkJobProcessor bulkJobProcessor;

    public BulkJobResponse createInvitationJob(InvitationBulkRequest request) {

        Long institutionId = UserContext.getInstitutionId();

        // Crear job en estado PENDING
        BulkJob job = jobService.createJob(
                "INVITATION",
                institutionId
        );

        // Disparar procesamiento ASYNC (no bloqueante)
        bulkJobProcessor.processInvitationJob(
                job.getId(),
                request.getRows(),
                institutionId
        );

        // Responder inmediatamente (SaaS behavior)
        return BulkJobResponse.builder()
                .jobId(job.getId())
                .jobType(job.getJobType())
                .status(job.getStatus().name()) // PENDING correcto
                .createdAt(job.getCreatedAt())
                .finishedAt(job.getFinishedAt())
                .build();
    }
}