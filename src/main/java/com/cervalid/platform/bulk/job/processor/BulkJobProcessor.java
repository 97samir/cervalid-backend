package com.cervalid.platform.bulk.job.processor;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkExecutionItemResponse;
import com.cervalid.platform.bulk.invitation.processor.InvitationBulkProcessor;
import com.cervalid.platform.bulk.job.entity.BulkJob;
import com.cervalid.platform.bulk.job.enums.BulkJobStatus;
import com.cervalid.platform.bulk.job.item.service.BulkJobItemResultService;
import com.cervalid.platform.bulk.job.service.BulkJobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BulkJobProcessor {

    private final InvitationBulkProcessor invitationProcessor;
    private final BulkJobService jobService;
    private final BulkJobItemResultService itemResultService;

    @Async
    public void processInvitationJob(
            Long jobId,
            List<InvitationBulkRowRequest> rows,
            Long institutionId) {

        log.info("STARTING BULK JOB: {}", jobId);

        try {
            jobService.updateStatus(
                    jobId,
                    BulkJobStatus.PROCESSING
            );

            int success = 0;
            int failed = 0;

            int rowNumber = 1;

            for (InvitationBulkRowRequest row : rows) {

                InvitationBulkExecutionItemResponse result =
                        invitationProcessor.process(
                                row,
                                institutionId,
                                rowNumber
                        );

                itemResultService.saveResult(
                        jobService.getJobEntity(jobId),
                        result.getRowNumber(),
                        result.getEmail(),
                        result.isSuccess(),
                        result.getMessage(),
                        result.isSuccess() ? "SENT_EMAIL" : "FAILED"
                );

                if (result.isSuccess()) {
                    success++;
                } else {
                    failed++;
                }

                rowNumber++;
            }

            jobService.updateCounters(
                    jobId,
                    success,
                    failed
            );

            jobService.updateStatus(
                    jobId,
                    BulkJobStatus.COMPLETED
            );

            log.info("BULK JOB COMPLETED: {}", jobId);

        } catch (Exception e) {

            log.error("ERROR PROCESSING BULK JOB {}", jobId, e);

            jobService.updateStatus(
                    jobId,
                    BulkJobStatus.FAILED
            );
        }
    }
}