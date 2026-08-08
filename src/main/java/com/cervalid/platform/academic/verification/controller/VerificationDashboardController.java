package com.cervalid.platform.academic.verification.controller;

import com.cervalid.platform.academic.verification.dto.response.VerificationDashboardResponse;
import com.cervalid.platform.academic.verification.service.VerificationDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/verification/dashboard")
@RequiredArgsConstructor
public class VerificationDashboardController {

    private final VerificationDashboardService service;

    @GetMapping
    public VerificationDashboardResponse dashboard() {
        return service.getDashboard();
    }
}