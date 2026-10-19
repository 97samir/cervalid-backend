package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.achievement.entity.Achievement;
import com.cervalid.platform.academic.achievement.enums.AchievementStatus;
import com.cervalid.platform.academic.achievement.enums.AchievementType;
import com.cervalid.platform.academic.achievement.repository.AchievementRepository;
import com.cervalid.platform.academic.achievement.service.AchievementTimelineService;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
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
public class DemoAchievementSeeder {

    private static final String UNIVERSITY_RUC = "DEMO-CERV-001";
    private static final String INSTITUTE_RUC = "DEMO-CERV-002";

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final StudentRepository studentRepository;
    private final AchievementRepository achievementRepository;
    private final PublicIdGenerator publicIdGenerator;
    private final AchievementTimelineService timelineService;

    @Transactional
    public void seed() {
        seedStudent(
                "demo.student01@cervalid.local",
                UNIVERSITY_RUC,
                universityAchievements()
        );

        seedStudent(
                "demo.student02@cervalid.local",
                UNIVERSITY_RUC,
                universityAchievements()
        );

        seedStudent(
                "demo.instituto.student01@cervalid.local",
                INSTITUTE_RUC,
                instituteAchievements()
        );

        seedStudent(
                "demo.instituto.student02@cervalid.local",
                INSTITUTE_RUC,
                instituteAchievements()
        );

        log.info("Demo achievement seeding completed.");
    }

    private void seedStudent(
            String email,
            String institutionRuc,
            List<AchievementSeed> achievements
    ) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElse(null);

        if (user == null || institution == null) {
            log.warn(
                    "Skipping demo achievements for {}: user or institution not found.",
                    email
            );
            return;
        }

        InstitutionUser membership = institutionUserRepository
                .findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                )
                .orElse(null);

        if (membership == null) {
            log.warn("Skipping demo achievements for {}: membership not found.", email);
            return;
        }

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElse(null);

        if (student == null) {
            log.warn("Skipping demo achievements for {}: student not found.", email);
            return;
        }

        for (AchievementSeed seed : achievements) {
            if (achievementRepository.existsByStudentIdAndTitle(
                    student.getId(),
                    seed.title()
            )) {
                continue;
            }

            Achievement achievement = Achievement.builder()
                    .publicId(publicIdGenerator.generate())
                    .institutionId(institution.getId())
                    .studentId(student.getId())
                    .title(seed.title())
                    .description(seed.description())
                    .type(seed.type())
                    .issuer(seed.issuer())
                    .achievedDate(seed.achievedDate())
                    .academicPeriod(seed.academicPeriod())
                    .status(AchievementStatus.ACTIVE)
                    .build();

            Achievement saved = achievementRepository.save(achievement);
            timelineService.created(saved);
        }
    }

    private List<AchievementSeed> universityAchievements() {
        return List.of(
                new AchievementSeed(
                        "Reconocimiento por rendimiento académico",
                        "Reconocimiento por el buen desempeño durante el periodo académico.",
                        AchievementType.AWARD,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2024, 7, 15),
                        "2024-I"
                ),
                new AchievementSeed(
                        "Proyecto universitario de programación",
                        "Participación destacada en el desarrollo de una solución de software.",
                        AchievementType.COMPETITION,
                        "Facultad de Ingeniería",
                        LocalDate.of(2024, 11, 20),
                        "2024-II"
                ),
                new AchievementSeed(
                        "Constancia de fundamentos de bases de datos",
                        "Acreditación complementaria en conceptos y diseño de bases de datos.",
                        AchievementType.CERTIFICATION,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2025, 7, 10),
                        "2025-I"
                ),
                new AchievementSeed(
                        "Participación en jornada de orientación vocacional",
                        "Apoyo voluntario en actividades de orientación para futuros estudiantes.",
                        AchievementType.VOLUNTEERING,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2025, 9, 5),
                        null
                ),
                new AchievementSeed(
                        "Participación en feria de innovación",
                        "Presentación de una propuesta tecnológica en una actividad institucional.",
                        AchievementType.RESEARCH,
                        "Comunidad de Innovación",
                        LocalDate.of(2025, 10, 18),
                        null
                ),
                new AchievementSeed(
                        "Reconocimiento por colaboración en equipo",
                        "Reconocimiento por responsabilidad y colaboración en proyectos académicos.",
                        AchievementType.AWARD,
                        "Docentes de la facultad",
                        LocalDate.of(2026, 3, 12),
                        null
                )
        );
    }

    private List<AchievementSeed> instituteAchievements() {
        return List.of(
                new AchievementSeed(
                        "Reconocimiento por desempeño en administración",
                        "Reconocimiento por el cumplimiento de objetivos académicos del ciclo.",
                        AchievementType.AWARD,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2024, 7, 12),
                        "2024-I"
                ),
                new AchievementSeed(
                        "Proyecto práctico de gestión empresarial",
                        "Participación en la elaboración de una propuesta de mejora administrativa.",
                        AchievementType.COMPETITION,
                        "Área de Administración",
                        LocalDate.of(2024, 11, 18),
                        "2024-II"
                ),
                new AchievementSeed(
                        "Constancia de contabilidad básica",
                        "Acreditación de conocimientos iniciales de registros contables.",
                        AchievementType.CERTIFICATION,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2025, 7, 8),
                        "2025-I"
                ),
                new AchievementSeed(
                        "Apoyo en actividad institucional",
                        "Colaboración voluntaria en la organización de una actividad educativa.",
                        AchievementType.VOLUNTEERING,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2025, 8, 22),
                        null
                ),
                new AchievementSeed(
                        "Participación en feria de emprendimiento",
                        "Presentación de una idea de negocio en una actividad institucional.",
                        AchievementType.COMPETITION,
                        "Comunidad de Emprendimiento",
                        LocalDate.of(2025, 10, 16),
                        null
                ),
                new AchievementSeed(
                        "Reconocimiento por comunicación efectiva",
                        "Reconocimiento por la presentación clara de propuestas y trabajos.",
                        AchievementType.AWARD,
                        "Docentes del instituto",
                        LocalDate.of(2026, 3, 10),
                        null
                )
        );
    }

    private record AchievementSeed(
            String title,
            String description,
            AchievementType type,
            String issuer,
            LocalDate achievedDate,
            String academicPeriod
    ) {
    }
}
