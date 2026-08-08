package com.cervalid.platform.audit.controller;

import com.cervalid.platform.audit.annotation.Auditable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class AuditTestController {

    @Auditable(action = "TEST", resource = "AUDIT")
    @PostMapping("/audit")
    public void testAudit() {
        System.out.println(">>> TEST AUDIT CONTROLLER");
    }
}
