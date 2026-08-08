package com.cervalid.platform.audit.service;

import com.cervalid.platform.audit.annotation.Auditable;
import org.springframework.stereotype.Service;

@Service
public class AuditProbeService {

    @Auditable(action = "PROBE", resource = "SYSTEM")
    public void probe() {
        System.out.println(">>> PROBE METHOD");
    }
}

