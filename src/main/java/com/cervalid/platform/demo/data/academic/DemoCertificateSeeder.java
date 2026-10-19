package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.enums.CertificateType;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.certificate.service.CertificateBuilderService;
import com.cervalid.platform.academic.certificate.service.CertificateGenerationService;
import com.cervalid.platform.academic.certificate.service.CertificateTimelineService;
import com.cervalid.platform.academic.certificate.service.CertificateValidationService;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.academic.transcript.service.TranscriptManagementService;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DemoCertificateSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final StudentRepository studentRepository;
    private final TranscriptRepository transcriptRepository;
    private final CertificateRepository certificateRepository;

    private final TranscriptManagementService transcriptManagementService;
    private final CertificateValidationService certificateValidationService;
    private final CertificateBuilderService certificateBuilderService;
    private final CertificateGenerationService certificateGenerationService;
    private final CertificateTimelineService certificateTimelineService;

    @Transactional
    public void seed() {
        seedStudent(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC
        );

        seedStudent(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC
        );

        seedStudent(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC
        );

        seedStudent(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC
        );

        log.info("Demo certificate seeding completed.");
    }

    private void seedStudent(
            String email,
            String institutionRuc
    ) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElse(null);

        if (user == null || institution == null) {
            log.warn(
                    "Skipping demo certificates for {}: user or institution not found.",
                    email
            );
            return;
        }

        InstitutionUser membership = membershipRepository
                .findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                )
                .orElse(null);

        if (membership == null) {
            log.warn(
                    "Skipping demo certificates for {}: membership not found.",
                    email
            );
            return;
        }

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElse(null);

        if (student == null) {
            log.warn(
                    "Skipping demo certificates for {}: student not found.",
                    email
            );
            return;
        }

        List<Transcript> transcripts =
                transcriptRepository.findByStudentIdAndInstitutionId(
                        student.getId(),
                        institution.getId()
                );

        for (Transcript transcript : transcripts) {
            issueCertificateIfMissing(transcript);
        }
    }

    private void issueCertificateIfMissing(
            Transcript transcript
    ) {
        if (certificateRepository.existsByTranscriptId(
                transcript.getId()
        )) {
            return;
        }

        if (transcript.getStatus() == TranscriptStatus.DRAFT) {
            transcriptManagementService.finalizeTranscript(
                    transcript.getPublicId(),
                    transcript.getInstitutionId()
            );

            // La finalización actualiza el transcript.
            transcript = transcriptRepository
                    .findByPublicId(transcript.getPublicId())
                    .orElseThrow(() -> new IllegalStateException(
                            "Transcript not found after finalization"
                    ));
        }

        try {
            certificateValidationService
                    .validateTranscriptForIssuance(transcript);
        } catch (RuntimeException exception) {
            log.warn(
                    "Skipping certificate for transcript {}: {}",
                    transcript.getPublicId(),
                    exception.getMessage()
            );
            return;
        }

        LocalDate awardedAt = resolveAwardedAt(
                transcript.getAcademicPeriod()
        );

        String title = "Certificado de estudios - "
                + transcript.getAcademicPeriod();

        Certificate certificate = certificateBuilderService.build(
                transcript,
                CertificateType.TRANSCRIPT,
                title,
                "Certificado demostrativo de estudios del periodo académico "
                        + transcript.getAcademicPeriod() + ".",
                awardedAt,
                null,
                null
        );

        Certificate saved = certificateGenerationService.generate(
                certificate,
                transcript
        );

        certificateTimelineService.issued(saved);

        log.info(
                "Demo certificate issued for transcript {}",
                transcript.getPublicId()
        );
    }

    private LocalDate resolveAwardedAt(String academicPeriod) {
        if (academicPeriod == null || academicPeriod.length() < 6) {
            return LocalDate.of(2025, 12, 15);
        }

        int year = Integer.parseInt(
                academicPeriod.substring(0, 4)
        );

        boolean firstSemester = academicPeriod.endsWith("-I");

        return firstSemester
                ? LocalDate.of(year, 7, 1)
                : LocalDate.of(year, 12, 15);
    }
}
