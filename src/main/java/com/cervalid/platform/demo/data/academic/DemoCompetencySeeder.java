package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.competency.entity.Competency;
import com.cervalid.platform.academic.competency.enums.CompetencyLevel;
import com.cervalid.platform.academic.competency.enums.CompetencySource;
import com.cervalid.platform.academic.competency.enums.CompetencyStatus;
import com.cervalid.platform.academic.competency.repository.CompetencyRepository;
import com.cervalid.platform.academic.competency.service.CompetencyTimelineService;
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
public class DemoCompetencySeeder {

    private static final String UNIVERSITY_RUC = "DEMO-CERV-001";
    private static final String INSTITUTE_RUC = "DEMO-CERV-002";

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final StudentRepository studentRepository;
    private final CompetencyRepository competencyRepository;
    private final PublicIdGenerator publicIdGenerator;
    private final CompetencyTimelineService timelineService;

    @Transactional
    public void seed() {
        seedStudent(
                "demo.student01@cervalid.local",
                UNIVERSITY_RUC,
                universityCompetencies()
        );

        seedStudent(
                "demo.student02@cervalid.local",
                UNIVERSITY_RUC,
                universityCompetencies()
        );

        seedStudent(
                "demo.instituto.student01@cervalid.local",
                INSTITUTE_RUC,
                instituteCompetencies()
        );

        seedStudent(
                "demo.instituto.student02@cervalid.local",
                INSTITUTE_RUC,
                instituteCompetencies()
        );

        log.info("Demo competency seeding completed.");
    }

    private void seedStudent(
            String email,
            String institutionRuc,
            List<CompetencySeed> competencies
    ) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElse(null);

        if (user == null || institution == null) {
            log.warn(
                    "Skipping demo competencies for {}: user or institution not found.",
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
            log.warn("Skipping demo competencies for {}: membership not found.", email);
            return;
        }

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElse(null);

        if (student == null) {
            log.warn("Skipping demo competencies for {}: student not found.", email);
            return;
        }

        for (CompetencySeed seed : competencies) {
            if (competencyRepository.existsByStudentIdAndNameAndLevel(
                    student.getId(),
                    seed.name(),
                    seed.level()
            )) {
                continue;
            }

            Competency competency = Competency.builder()
                    .publicId(publicIdGenerator.generate())
                    .institutionId(institution.getId())
                    .studentId(student.getId())
                    .name(seed.name())
                    .description(seed.description())
                    .level(seed.level())
                    .status(CompetencyStatus.ACTIVE)
                    .source(CompetencySource.MANUAL)
                    .issuer(seed.issuer())
                    .acquiredDate(seed.acquiredDate())
                    .evidenceReference(null)
                    .evidenceType(null)
                    .academicPeriod(seed.academicPeriod())
                    .build();

            Competency saved = competencyRepository.save(competency);
            timelineService.createdManually(saved);
        }
    }

    private List<CompetencySeed> universityCompetencies() {
        return List.of(
                new CompetencySeed(
                        "Fundamentos de programación",
                        "Comprende variables, estructuras de control y funciones.",
                        CompetencyLevel.INTERMEDIATE,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2024, 6, 20),
                        "2024-I"
                ),
                new CompetencySeed(
                        "Modelado de bases de datos",
                        "Diseña modelos relacionales y aplica conceptos de normalización.",
                        CompetencyLevel.INTERMEDIATE,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2024, 11, 15),
                        "2024-II"
                ),
                new CompetencySeed(
                        "Resolución de problemas matemáticos",
                        "Aplica razonamiento lógico y herramientas matemáticas a problemas de ingeniería.",
                        CompetencyLevel.BASIC,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2025, 6, 25),
                        "2025-I"
                ),
                new CompetencySeed(
                        "Trabajo colaborativo",
                        "Colabora con otras personas para cumplir objetivos compartidos.",
                        CompetencyLevel.ADVANCED,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2025, 8, 15),
                        null
                ),
                new CompetencySeed(
                        "Comunicación técnica",
                        "Explica ideas y resultados técnicos de forma clara y estructurada.",
                        CompetencyLevel.INTERMEDIATE,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2025, 10, 10),
                        null
                ),
                new CompetencySeed(
                        "Gestión del aprendizaje autónomo",
                        "Organiza su aprendizaje y busca recursos para resolver nuevos desafíos.",
                        CompetencyLevel.ADVANCED,
                        "Universidad Demo Cervalid",
                        LocalDate.of(2026, 3, 5),
                        null
                )
        );
    }

    private List<CompetencySeed> instituteCompetencies() {
        return List.of(
                new CompetencySeed(
                        "Fundamentos de administración",
                        "Comprende los principios básicos de planificación y organización.",
                        CompetencyLevel.INTERMEDIATE,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2024, 6, 18),
                        "2024-I"
                ),
                new CompetencySeed(
                        "Registro de operaciones contables",
                        "Registra operaciones sencillas aplicando criterios contables básicos.",
                        CompetencyLevel.INTERMEDIATE,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2024, 11, 12),
                        "2024-II"
                ),
                new CompetencySeed(
                        "Comunicación empresarial",
                        "Redacta comunicaciones profesionales y presenta información de manera organizada.",
                        CompetencyLevel.BASIC,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2025, 6, 20),
                        "2025-I"
                ),
                new CompetencySeed(
                        "Atención al cliente",
                        "Identifica necesidades y propone respuestas orientadas al servicio.",
                        CompetencyLevel.ADVANCED,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2025, 8, 12),
                        null
                ),
                new CompetencySeed(
                        "Organización documental",
                        "Clasifica y mantiene documentos administrativos de forma ordenada.",
                        CompetencyLevel.INTERMEDIATE,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2025, 10, 8),
                        null
                ),
                new CompetencySeed(
                        "Trabajo en equipo",
                        "Coordina tareas y responsabilidades para alcanzar objetivos comunes.",
                        CompetencyLevel.ADVANCED,
                        "Instituto Demo Cervalid",
                        LocalDate.of(2026, 3, 3),
                        null
                )
        );
    }

    private record CompetencySeed(
            String name,
            String description,
            CompetencyLevel level,
            String issuer,
            LocalDate acquiredDate,
            String academicPeriod
    ) {
    }
}
