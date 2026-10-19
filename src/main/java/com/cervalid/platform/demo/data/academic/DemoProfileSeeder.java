package com.cervalid.platform.demo.data.academic;

import com.cervalid.platform.academic.profile.dto.internal.CreateAcademicProfileCommand;
import com.cervalid.platform.academic.profile.enums.AcademicFaculty;
import com.cervalid.platform.academic.profile.enums.AcademicProgram;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.profile.service.AcademicProfileManagementService;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
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

@Service
@RequiredArgsConstructor
public class DemoProfileSeeder {

    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;
    private final InstitutionUserRepository membershipRepository;
    private final StudentRepository studentRepository;
    private final AcademicProfileRepository profileRepository;
    private final AcademicProfileManagementService profileManagementService;

    @Transactional
    public void seed() {
        seedProfile(
                "demo.student01@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                AcademicProgram.DISENO_DESARROLLO_SOFTWARE,
                AcademicFaculty.INGENIERIA_Y_TECNOLOGIA,
                5,
                "2026-I"
        );

        seedProfile(
                "demo.student02@cervalid.local",
                DemoInstitutionSeeder.UNIVERSITY_RUC,
                AcademicProgram.ARQUITECTURA_SISTEMAS_INFORMACION,
                AcademicFaculty.INGENIERIA_Y_TECNOLOGIA,
                3,
                "2026-I"
        );

        seedProfile(
                "demo.instituto.student01@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                AcademicProgram.ADMINISTRACION_EMPRESAS,
                AcademicFaculty.CIENCIAS_EMPRESARIALES,
                4,
                "2026-I"
        );

        seedProfile(
                "demo.instituto.student02@cervalid.local",
                DemoInstitutionSeeder.INSTITUTE_RUC,
                AcademicProgram.CONTABILIDAD,
                AcademicFaculty.CIENCIAS_EMPRESARIALES,
                2,
                "2026-I"
        );
    }

    private void seedProfile(
            String email,
            String institutionRuc,
            AcademicProgram program,
            AcademicFaculty faculty,
            int currentCycle,
            String academicPeriod) {

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
                        "No existe la pertenencia de: " + email
                ));

        Student student = studentRepository
                .findByInstitutionMembershipId(membership.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "No existe el estudiante de: " + email
                ));

        if (profileRepository.existsByStudentId(student.getId())) {
            return;
        }

        CreateAcademicProfileCommand command =
                CreateAcademicProfileCommand.builder()
                        .studentId(student.getId())
                        .institutionId(institution.getId())
                        .program(program)
                        .faculty(faculty)
                        .modality("PRESENCIAL")
                        .currentCycle(currentCycle)
                        .advisor("Tutor Demo")
                        .academicPeriod(academicPeriod)
                        .build();

        profileManagementService.create(command);
    }
}