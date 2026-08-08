package com.cervalid.platform.audit.entity;

import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class SoftDeleteEntity extends AuditableEntity {

    protected boolean deleted;
    protected LocalDateTime deletedAt;
    protected Long deletedBy;
}
