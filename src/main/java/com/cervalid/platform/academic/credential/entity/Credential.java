package com.cervalid.platform.academic.credential.entity;

import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "academic_credentials",
        indexes = {
                @Index(name = "idx_credential_public_id", columnList = "public_id"),
                @Index(name = "idx_credential_institution_id", columnList = "institution_id"),
                @Index(name = "idx_credential_student_id", columnList = "student_id"),
                @Index(name = "idx_credential_status", columnList = "status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Credential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, updatable = false)
    private UUID publicId;

    @Column(name = "institution_id", nullable = false)
    private Long institutionId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "credential_number", nullable = false, unique = true, length = 30)
    private String credentialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private CredentialType type;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "awarded_at", nullable = false)
    private LocalDate awardedAt;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CredentialStatus status;

    @Column(name = "document_hash", length = 128)
    private String documentHash;

    @Column(name = "document_url", length = 500)
    private String documentUrl;

    @Column(name = "blockchain_tx_hash", length = 255)
    private String blockchainTxHash;

    @Column(name = "blockchain_network", length = 100)
    private String blockchainNetwork;

    @Column(name = "block_number")
    private Long blockNumber;

    @Column(name = "anchored_at")
    private LocalDateTime anchoredAt;

    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "revocation_reason", length = 1000)
    private String revocationReason;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }

        if (status == null) {
            status = CredentialStatus.DRAFT;
        }

        deleted = false;
    }
}