package com.cervalid.platform.academic.certificate.entity;

import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "academic_certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;
    private Long studentId;
    private Long transcriptId;

    @Column(nullable = false)
    private Long institutionId;

    private String certificateNumber;

    @Enumerated(EnumType.STRING)
    private CertificateStatus status;

    @Enumerated(EnumType.STRING)
    private CertificateType type;

    @Column(nullable = false)
    private String certificateHash;

    private String transcriptHash;

    @Column(nullable = false)
    private String verificationHash;

    private String verificationUrl;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode snapshotJson;

    private LocalDateTime issuedAt;
    private LocalDateTime revokedAt;

    private String blockchainTxHash;
    private String network;
    private Long blockNumber;
    private LocalDateTime anchoredAt; // hora que fue anclado

    @Builder.Default
    private boolean deleted = false;
}
