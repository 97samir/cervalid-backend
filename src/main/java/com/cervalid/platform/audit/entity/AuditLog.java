package com.cervalid.platform.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;          // LOGIN, CREATE_USER, ISSUE_CERT
    private String resource;        // USER, CERTIFICATE, INSTITUTION
    private String resourceId;

    private Long actorUserId;
    private String actorEmail;

    @Column(nullable = true)
    private Long institutionId;

    private LocalDateTime createdAt;
    private String ipAddress;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
