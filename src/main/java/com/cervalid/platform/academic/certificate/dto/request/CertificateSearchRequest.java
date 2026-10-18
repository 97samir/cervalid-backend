package com.cervalid.platform.academic.certificate.dto.request;

import com.cervalid.platform.academic.certificate.enums.CertificateStatus;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class CertificateSearchRequest {

    private UUID studentPublicId;
    private String certificateNumber;
    private CertificateStatus status;
    private CertificateType type;

    private String studentCode;
    private AcademicProgram program;
    private LocalDate fromDate;
    private LocalDate toDate;
}