package com.cervalid.platform.academic.certificate.snapshot;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CertificateSnapshotBuilder {

    private final StudentRepository studentRepository;
    private final AcademicProfileRepository profileRepository;
    private final InstitutionRepository institutionRepository;
    private final ObjectMapper objectMapper;
    private final InstitutionUserRepository institutionUserRepository;

    public JsonNode build(
            Certificate certificate) {

        Long studentId = certificate.getStudentId();
        Long institutionId = certificate.getInstitutionId();

        Student student =
                studentRepository.findById(studentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Student not found. studentId="
                                                + studentId));

        InstitutionUser membership =
                institutionUserRepository
                        .findById(student.getInstitutionMembershipId())
                        .orElse(null);

        User user =
                membership != null
                        ? membership.getUser()
                        : null;

        String studentName = null;

        if (user != null) {
            studentName =
                    user.getName() + " " + user.getLastName();
        }

        AcademicProfile profile =
                profileRepository.findByStudentId(studentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "AcademicProfile not found. studentId="
                                                + studentId));

        Institution institution =
                institutionRepository.findById(institutionId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Institution not found. institutionId="
                                                + institutionId));

        Map<String, Object> snapshot =
                Map.of(
                        "certificate", Map.of(
                                "publicId", certificate.getPublicId(),
                                "certificateNumber",
                                certificate.getCertificateNumber(),

                                "type",
                                certificate.getType(),

                                "title",
                                certificate.getTitle(),

                                "awardedAt",
                                certificate.getAwardedAt(),

                                "issuedAt",
                                certificate.getIssuedAt()
                        ),

                        "student", Map.of(
                                "publicId",
                                student.getPublicId(),

                                "studentCode",
                                student.getStudentCode(),

                                "name",
                                studentName
                        ),

                        "institution", Map.of(
                                "id",
                                institution.getId(),

                                "name",
                                institution.getName()
                        ),

                        "academic", Map.of(
                                "program",
                                profile.getProgram(),

                                "faculty",
                                profile.getFaculty(),

                                "modality",
                                profile.getModality(),

                                "currentCycle",
                                profile.getCurrentCycle()
                        )
                );

        return objectMapper.valueToTree(snapshot);
    }
}