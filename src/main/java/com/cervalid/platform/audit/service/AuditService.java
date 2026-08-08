package com.cervalid.platform.audit.service;

import com.cervalid.platform.audit.entity.AuditLog;
import com.cervalid.platform.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(AuditLog log) {

        try {
            //System.out.println(">>> GUARDANDO AUDIT LOG");
            auditLogRepository.save(log);
            //System.out.println(">>> AUDIT LOG GUARDADO");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AUDIT ERROR: "+ e.getMessage());
        }
    }
}
