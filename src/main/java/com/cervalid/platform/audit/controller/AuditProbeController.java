package com.cervalid.platform.audit.controller;

import com.cervalid.platform.audit.service.AuditProbeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/probe")
public class AuditProbeController {

    private final AuditProbeService service;

    public AuditProbeController(AuditProbeService service) {
        this.service = service;
    }

    @GetMapping
    public void probe() {
        service.probe();
    }
}

