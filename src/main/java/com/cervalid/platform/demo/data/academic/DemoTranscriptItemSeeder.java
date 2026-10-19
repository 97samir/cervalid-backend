package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DemoTranscriptItemSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final StudentRepository studentRepository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptItemRepository itemRepository;
    private final PublicIdGenerator publicIdGenerator;
    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder metadataBuilder;

    @Transactional
    public void seed() {
        seedStudentPeriods(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                List.of("2024-I", "2024-II")
        );

        seedStudentPeriods(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                List.of("2025-I", "2025-II")
        );

        seedStudentPeriods(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                List.of("2024-I", "2024-II")
        );

        seedStudentPeriods(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                List.of("2025-I", "2025-II")
        );
    }

    private void seedStudentPeriods(
            String email,
            String institutionRuc,
            List<String> periods) {

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

        for (String period : periods) {
            Transcript transcript = transcriptRepository
                    .findByStudentId(student.getId())
                    .stream()
                    .filter(t -> t.getInstitutionId().equals(institution.getId()))
                    .filter(t -> t.getAcademicPeriodType() == AcademicPeriodType.SEMESTER)
                    .filter(t -> t.getAcademicPeriod().equals(period))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException(
                            "No existe el historial " + period
                                    + " del estudiante " + email
                    ));

            seedItems(transcript, institutionRuc);
        }
    }

    private void seedItems(
            Transcript transcript,
            String institutionRuc) {

        List<TranscriptItem> existingItems =
                itemRepository.findByTranscriptId(transcript.getId());

        boolean wasEmpty = existingItems.isEmpty();

        List<CourseSeed> courses = getCourses(institutionRuc);

        for (CourseSeed course : courses) {
            if (itemRepository.existsByTranscriptIdAndCourseCode(
                    transcript.getId(),
                    course.code())) {
                continue;
            }

            TranscriptItem item = TranscriptItem.builder()
                    .publicId(publicIdGenerator.generate())
                    .transcriptId(transcript.getId())
                    .courseCode(course.code())
                    .courseName(course.name())
                    .credits(course.credits())
                    .grade(course.grade())
                    .build();

            itemRepository.save(item);
        }

        if (wasEmpty) {
            publishTranscriptCreated(transcript, courses.size());
        }
    }

    private List<CourseSeed> getCourses(String institutionRuc) {
        if (DemoInstitutionSeeder.UNIVERSITY_RUC.equals(institutionRuc)) {
            return List.of(
                    new CourseSeed(
                            "PROG-101",
                            "Fundamentos de Programación",
                            4,
                            new BigDecimal("17.50")
                    ),
                    new CourseSeed(
                            "BD-101",
                            "Fundamentos de Bases de Datos",
                            4,
                            new BigDecimal("16.00")
                    ),
                    new CourseSeed(
                            "MAT-101",
                            "Matemática para Ingeniería",
                            3,
                            new BigDecimal("15.50")
                    )
            );
        }

        return List.of(
                new CourseSeed(
                        "ADM-101",
                        "Fundamentos de Administración",
                        4,
                        new BigDecimal("17.00")
                ),
                new CourseSeed(
                        "CON-101",
                        "Contabilidad Básica",
                        4,
                        new BigDecimal("16.50")
                ),
                new CourseSeed(
                        "COM-101",
                        "Comunicación Empresarial",
                        3,
                        new BigDecimal("18.00")
                )
        );
    }

    private void publishTranscriptCreated(
            Transcript transcript,
            int courseCount) {

        JsonNode metadata = metadataBuilder.build(
                Map.of(
                        "period", transcript.getAcademicPeriod(),
                        "courses", courseCount
                )
        );

        timelineEventService.createEvent(
                transcript.getInstitutionId(),
                transcript.getStudentId(),
                TimelineEventType.TRANSCRIPT_CREATED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Transcript generated",
                "Academic transcript created",
                transcript.getPublicId(),
                TimelineReferenceType.TRANSCRIPT,
                transcript.getAcademicPeriod(),
                metadata
        );
    }

    private record CourseSeed(
            String code,
            String name,
            Integer credits,
            BigDecimal grade) {
    }
}