package com.cervalid.platform.academic.verification.proof;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "academic_verification_proofs")
public class VerificationProof {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateNumber;
    private String transcriptHash;
    private String certificateHash;
    private String verificationHash;
    private boolean valid;
    private LocalDateTime createdAt;
}
