package com.cervalid.platform.academic.verification.controller;

import com.cervalid.platform.academic.verification.dto.request.VerificationSummaryFilterRequest;
import com.cervalid.platform.academic.verification.dto.response.VerificationSummaryResponse;
import com.cervalid.platform.academic.verification.service.VerificationSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification/summary")
@RequiredArgsConstructor
public class VerificationSummaryController {

    private final VerificationSummaryService service;

    @GetMapping public Page<VerificationSummaryResponse> search(
            @ModelAttribute
            VerificationSummaryFilterRequest request,
            Pageable pageable) {
        return service.search( request, pageable );
    }

}