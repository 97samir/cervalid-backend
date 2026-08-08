package com.cervalid.platform.bulk.invitation.controller;

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkExecuteRequest;
import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRequest;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkExecutionResponse;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkPreviewResponse;
import com.cervalid.platform.bulk.invitation.service.InvitationBulkExecutionService;
import com.cervalid.platform.bulk.invitation.service.InvitationBulkPreviewService;
import com.cervalid.platform.bulk.invitation.service.InvitationBulkService;
import com.cervalid.platform.bulk.job.dto.BulkJobResponse;
import com.cervalid.platform.security.context.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bulk/invitations")
@RequiredArgsConstructor
public class InvitationBulkController {

    private final InvitationBulkPreviewService previewService;
    private final InvitationBulkExecutionService executionService;
    private final InvitationBulkService invitationBulkService;

    // ver previsualizacion de archivo excel
    @PostMapping(
            value = "/preview",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<InvitationBulkPreviewResponse> preview(
            @RequestParam("file") MultipartFile file) {

        Long institutionId = UserContext.getInstitutionId();

        return ResponseEntity.ok(
                previewService.preview(file, institutionId)
        );
    }

    // invitacion
    @PostMapping("/jobs")
    public BulkJobResponse createJob(
            @RequestBody InvitationBulkRequest request) {
        return invitationBulkService
                .createInvitationJob(request);
    }

}