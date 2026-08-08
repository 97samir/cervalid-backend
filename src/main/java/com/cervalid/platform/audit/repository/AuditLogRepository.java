package com.cervalid.platform.audit.repository;

import com.cervalid.platform.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {
}
