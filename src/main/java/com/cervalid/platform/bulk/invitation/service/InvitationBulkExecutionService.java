package com.cervalid.platform.bulk.invitation.service;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkExecuteRequest;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkExecutionItemResponse;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkExecutionResponse;
import com.cervalid.platform.bulk.invitation.processor.InvitationBulkProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationBulkExecutionService {

    private final InvitationBulkProcessor processor;

    public InvitationBulkExecutionResponse execute(
            InvitationBulkExecuteRequest request) {

        List<InvitationBulkExecutionItemResponse> results = new ArrayList<>();

        int success = 0;
        int failed = 0;

        int rowNumber = 1;

        for (var row : request.getRows()) {

            InvitationBulkExecutionItemResponse result =
                    processor.process(
                            row,
                            request.getInstitutionId(),
                            rowNumber
                    );

            if (result.isSuccess()) {
                success++;
            } else {
                failed++;
            }

            results.add(result);
            rowNumber++;
        }

        return InvitationBulkExecutionResponse.builder()
                .totalProcessed(results.size())
                .successCount(success)
                .failedCount(failed)
                .results(results)
                .build();
    }
}