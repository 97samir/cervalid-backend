package com.cervalid.platform.academic.verification.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.verification.dto.view.VerificationHistoryView;
import com.cervalid.platform.academic.verification.dto.view.VerificationView;
import com.cervalid.platform.academic.verification.entity.VerificationRecord;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationQueryService {

    private final StudentRepository studentRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final InstitutionRepository institutionRepository;
    private final AcademicProfileRepository academicProfileRepository;

    public VerificationView buildVerificationView(
            Long studentId,
            Long institutionId) {

        AcademicProfile profile = academicProfileRepository
                .findByStudentId(studentId)
                .orElse(null);

        return VerificationView.builder()
                .studentName(resolveStudentName(studentId))
                .institutionName(resolveInstitutionName(institutionId))
                .program(profile != null ? profile.getProgram() : null)
                .faculty(profile != null ? profile.getFaculty() : null)
                .modality(profile != null ? profile.getModality() : null)
                .currentCycle(profile != null ? profile.getCurrentCycle() : null)
                .build();
    }

    public String resolveStudentName(Long studentId) {

        Student student = studentRepository
                .findById(studentId)
                .orElse(null);

        if (student == null) {
            return null;
        }

        InstitutionUser membership = institutionUserRepository
                        .findById(student.getInstitutionMembershipId())
                        .orElse(null);

        if (membership == null) {
            return null;
        }

        User user = membership.getUser();

        if (user == null) {
            return null;
        }

        return user.getName() + " " + user.getLastName();
    }

    public String resolveInstitutionName(Long institutionId) {

        if (institutionId == null) {
            return null;
        }

        return institutionRepository
                .findById(institutionId)
                .map(Institution::getName)
                .orElse(null);
    }

    public VerificationHistoryView buildHistoryView(
            VerificationRecord record) {

        Certificate certificate = record.getCertificate();
        VerificationView verificationView = null;

        if (certificate != null) {
            verificationView = buildVerificationView(
                    certificate.getStudentId(),
                    certificate.getInstitutionId()
            );
        }

        return VerificationHistoryView.builder()
                .publicId(record.getPublicId())
                .certificatePublicId(certificate != null
                        ? certificate.getPublicId()
                        : null)

                .certificateNumber(record.getCertificateNumber())

                .certificateType(certificate != null
                        && certificate.getType() != null
                        ? certificate.getType().name()
                        : null)

                .studentName(verificationView != null
                        ? verificationView.getStudentName()
                        : null)

                .institutionName(verificationView != null
                        ? verificationView.getInstitutionName()
                        : null)

                .verificationReason(record.getVerificationReason())
                .status(record.getStatus().name())
                .verificationSource(record.getVerificationSource().name())
                .ip(record.getVerifiedByIp())
                .verifiedAt(record.getVerifiedAt())
                .build();
    }
}