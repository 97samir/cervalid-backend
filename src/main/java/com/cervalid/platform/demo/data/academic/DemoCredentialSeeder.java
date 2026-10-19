package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.credential.service.CredentialManagementService;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
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
public class DemoCredentialSeeder {

    private static final String UNIVERSITY_RUC = "DEMO-CERV-001";
    private static final String INSTITUTE_RUC = "DEMO-CERV-002";

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository institutionUserRepository;
    private final StudentRepository studentRepository;
    private final CredentialRepository credentialRepository;
    private final CredentialManagementService credentialManagementService;

    @Transactional
    public void seed() {
        seedStudent(
                "demo.student01@cervalid.local",
                UNIVERSITY_RUC,
                universityCredentials()
        );

        seedStudent(
                "demo.student02@cervalid.local",
                UNIVERSITY_RUC,
                universityCredentials()
        );

        seedStudent(
                "demo.instituto.student01@cervalid.local",
                INSTITUTE_RUC,
                instituteCredentials()
        );

        seedStudent(
                "demo.instituto.student02@cervalid.local",
                INSTITUTE_RUC,
                instituteCredentials()
        );

        log.info("Demo credential seeding completed.");
    }

    private void seedStudent(
            String email,
            String institutionRuc,
            List<CredentialSeed> credentials
    ) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElse(null);

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElse(null);

        if (user == null || institution == null) {
            log.warn(
                    "Skipping demo credentials for {}: user or institution not found.",
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
            log.warn(
                    "Skipping demo credentials for {}: membership not found.",
                    email
            );
            return;
        }

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElse(null);

        if (student == null) {
            log.warn(
                    "Skipping demo credentials for {}: student not found.",
                    email
            );
            return;
        }

        for (CredentialSeed seed : credentials) {
            boolean exists = credentialRepository
                    .existsByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                            institution.getId(),
                            student.getId(),
                            seed.type()
                    );

            if (exists) {
                continue;
            }

            CreateCredentialRequest request =
                    CreateCredentialRequest.builder()
                            .title(seed.title())
                            .description(seed.description())
                            .type(seed.type())
                            .awardedAt(seed.awardedAt())
                            .build();

            Credential created = credentialManagementService.create(
                    institution.getId(),
                    student.getPublicId(),
                    request
            );

            log.debug(
                    "Created demo credential {} for student {}",
                    created.getCredentialNumber(),
                    email
            );
        }
    }

    private List<CredentialSeed> universityCredentials() {
        return List.of(
                new CredentialSeed(
                        CredentialType.BACHELOR_DEGREE,
                        "Grado académico de bachiller",
                        "Credencial demostrativa de grado académico universitario.",
                        LocalDate.of(2025, 12, 15)
                ),
                new CredentialSeed(
                        CredentialType.PROFESSIONAL_TITLE,
                        "Título profesional",
                        "Credencial demostrativa de título profesional.",
                        LocalDate.of(2026, 3, 20)
                )
        );
    }

    private List<CredentialSeed> instituteCredentials() {
        return List.of(
                new CredentialSeed(
                        CredentialType.BACHELOR_DEGREE,
                        "Credencial académica de formación",
                        "Registro demostrativo de una credencial académica de formación.",
                        LocalDate.of(2025, 12, 10)
                ),
                new CredentialSeed(
                        CredentialType.PROFESSIONAL_TITLE,
                        "Credencial de título profesional",
                        "Registro demostrativo de una credencial de título profesional.",
                        LocalDate.of(2026, 3, 15)
                )
        );
    }

    private record CredentialSeed(
            CredentialType type,
            String title,
            String description,
            LocalDate awardedAt
    ) {
    }
}
