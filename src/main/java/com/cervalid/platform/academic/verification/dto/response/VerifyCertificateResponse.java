package com.cervalid.platform.academic.verification.dto.response;

import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import com.cervalid.platform.academic.timeline.dto.view.PublicTimelineEventView;
import com.cervalid.platform.academic.verification.enums.VerificationLevel;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyCertificateResponse {

    private boolean valid;
    private String verificationStatus;

    private UUID certificatePublicId;
    private String certificateNumber;
    private String studentName;
    private String institutionName;

    private AcademicProgram program;
    private AcademicFaculty faculty;
    private String modality;
    private Integer currentCycle;
    private String certificateType;

    private String documentHash;
    private String documentUrl;

    private String issuedAt;
    private String awardedAt;

    private List<PublicTimelineEventView> timeline;

    private String verificationMessage;
    private VerificationLevel verificationLevel;

}
