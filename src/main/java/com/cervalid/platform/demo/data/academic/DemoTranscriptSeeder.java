package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DemoTranscriptSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final StudentRepository studentRepository;
    private final TranscriptRepository transcriptRepository;
    private final PublicIdGenerator publicIdGenerator;

    @Transactional
    public void seed() {
        // Universidad: estudiante que ingresó en 2024.
        seedTranscript(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "2024-I"
        );

        seedTranscript(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "2024-II"
        );

        // Universidad: estudiante que ingresó en 2025.
        seedTranscript(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "2025-I"
        );

        seedTranscript(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "2025-II"
        );

        // Instituto: estudiante que ingresó en 2024.
        seedTranscript(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "2024-I"
        );

        seedTranscript(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "2024-II"
        );

        // Instituto: estudiante que ingresó en 2025.
        seedTranscript(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "2025-I"
        );

        seedTranscript(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "2025-II"
        );
    }

    private void seedTranscript(
            String email,
            String institutionRuc,
            String academicPeriod) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el usuario demo: " + email
                ));

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe la institución: " + institutionRuc
                ));

        InstitutionUser membership = membershipRepository
                .findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                )
                .orElseThrow(() -> new IllegalStateException(
                        "No existe la pertenencia de: " + email
                ));

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el estudiante de: " + email
                ));

        boolean exists = transcriptRepository
                .existsByStudentIdAndAcademicPeriodTypeAndAcademicPeriod(
                        student.getId(),
                        AcademicPeriodType.SEMESTER,
                        academicPeriod
                );

        if (exists) {
            return;
        }

        Transcript transcript = Transcript.builder()
                .publicId(publicIdGenerator.generate())
                .studentId(student.getId())
                .institutionId(institution.getId())
                .academicPeriod(academicPeriod)
                .academicPeriodType(AcademicPeriodType.SEMESTER)
                .status(TranscriptStatus.DRAFT)
                .build();

        transcriptRepository.save(transcript);
    }
}