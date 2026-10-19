package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.student.dto.internal.CreateStudentCommand;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.student.service.StudentService;
import com.cervalid.platform.demo.data.institution.DemoInstitutionSeeder;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DemoStudentSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final StudentRepository studentRepository;
    private final StudentService studentService;

    @Transactional
    public void seed() {
        seedStudent(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "UNI-DEMO-001",
                LocalDate.of(2024, 3, 1)
        );

        seedStudent(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                "UNI-DEMO-002",
                LocalDate.of(2025, 3, 1)
        );

        seedStudent(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "INS-DEMO-001",
                LocalDate.of(2024, 4, 1)
        );

        seedStudent(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                "INS-DEMO-002",
                LocalDate.of(2025, 4, 1)
        );
    }

    private void seedStudent(
            String email,
            String institutionRuc,
            String studentCode,
            LocalDate admissionDate) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el usuario demo: " + email
                ));

        Institution institution = institutionRepository
                .findByRuc(institutionRuc)
                .orElseThrow(() -> new IllegalStateException(
                        "No existe la institución demo: " + institutionRuc
                ));

        InstitutionUser membership = membershipRepository
                .findByUserIdAndInstitutionId(
                        user.getId(),
                        institution.getId()
                )
                .orElseThrow(() -> new IllegalStateException(
                        "No existe la pertenencia institucional de: " + email
                ));

        if (studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .isPresent()) {
            return;
        }

        CreateStudentCommand command = CreateStudentCommand.builder()
                .userId(user.getId())
                .institutionId(institution.getId())
                .institutionMembershipId(membership.getId())
                .studentCode(studentCode)
                .admissionDate(admissionDate)
                .graduationDate(null)
                .build();

        studentService.createStudent(
                command,
                StudentStatus.ACTIVE
        );
    }
}