package com.cervalid.platform.academic.certificate.dto.request;

import com.cervalid.platform.academic.certificate.enums.CertificateType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueCertificateRequest {

    // private Long studentId; // ya se obtiene del transcriptId
    private UUID transcriptPublicId;
    private CertificateType type;

    @NotBlank
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    private LocalDate awardedAt; // localDate solo fecha no instancia de seg

    private String documentHash;
    private String documentUrl;


}