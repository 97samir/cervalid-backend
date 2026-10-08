package com.cervalid.platform.academic.verification.entity;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.verification.enums.VerificationSource;
import com.cervalid.platform.academic.verification.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "academic_verifications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID publicId;

    @Column(name = "credential_id")
    private Long credentialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credential_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    private Credential credential;

    @Column(name = "certificate_id")
    private Long certificateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    private Certificate certificate;

    private String credentialNumber;
    private String certificateNumber;
    private Long institutionId;
    private String providedHash;
    private String storedHash;
    private boolean valid;

    @Column(length = 500)
    private String verificationReason;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;

    @Enumerated(EnumType.STRING)
    private VerificationSource verificationSource;

    private String verifiedByIp;
    private LocalDateTime verifiedAt;
    //private String blockchainTxHash;
}
