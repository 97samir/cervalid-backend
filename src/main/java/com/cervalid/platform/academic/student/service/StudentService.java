package com.cervalid.platform.academic.student.service;

import com.cervalid.platform.academic.student.domain.StudentBusinessRules;
import com.cervalid.platform.academic.student.domain.StudentCodeNormalizer;
import com.cervalid.platform.academic.student.domain.StudentCodeValidator;
import com.cervalid.platform.academic.student.dto.internal.CreateStudentCommand;
import com.cervalid.platform.academic.student.dto.request.CreateStudentRequest;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.enums.StudentStatus;
import com.cervalid.platform.academic.student.event.StudentCreatedEvent;
import com.cervalid.platform.academic.student.event.StudentEventPublisher;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.academic.timeline.domain.TimelineMetadataBuilder;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import com.cervalid.platform.security.context.SecurityContextService;
import com.cervalid.platform.shared.identity.PublicIdGenerator;
import com.cervalid.platform.user.entity.InstitutionUser;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.InstitutionUserRepository;
import com.cervalid.platform.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentEventPublisher eventPublisher;
    private final PublicIdGenerator publicIdGenerator;
    private final StudentCodeValidator studentCodeValidator;
    private final StudentCodeNormalizer studentCodeNormalizer;
    private final StudentBusinessRules businessRules;
    private final TimelineEventService timelineEventService;
    private final TimelineMetadataBuilder timelineMetadataBuilder;

    public Student createStudent(
            CreateStudentCommand request,
            StudentStatus status) {

        studentRepository
                .findByInstitutionMembershipId(
                        request.getInstitutionMembershipId())
                .ifPresent(student -> {

                    throw new RuntimeException(
                            "Student already exists for membership");
                });

        studentCodeValidator.validate(
                request.getStudentCode());

        businessRules.validateAdmissionDate(
                request.getAdmissionDate());

        String normalizedCode =
                studentCodeNormalizer.normalize(
                        request.getStudentCode());

        // validar
        if (studentRepository
                .existsByInstitutionIdAndStudentCode(
                        request.getInstitutionId(),
                        normalizedCode)
        ) {

            throw new RuntimeException(
                    "Student code already exists in institution");
        }

        Student student = Student.builder()
                .publicId(publicIdGenerator.generate())
                .userId(request.getUserId())
                .institutionId(request.getInstitutionId())
                .institutionMembershipId(request.getInstitutionMembershipId())
                .studentCode(normalizedCode)
                .status(status)
                .graduationDate(request.getGraduationDate())
                .admissionDate(request.getAdmissionDate())
                .build();

        Student saved = studentRepository.save(student);

        publishStudentCreated(saved);

        return saved;
    }

    public Student createActiveStudent(
            CreateStudentCommand command) {

        return createStudent(
                command,
                StudentStatus.ACTIVE
        );
    }

    public Student createPendingStudent(
            CreateStudentCommand command) {

        return createStudent(
                command,
                StudentStatus.PENDING_ACTIVATION
        );
    }

    private void publishStudentCreated(
            Student student) {

        JsonNode metadata = timelineMetadataBuilder.build(
                Map.of(
                        "studentCode", student.getStudentCode(),
                        "admissionDate", student.getAdmissionDate(),
                        "graduationDate", student.getGraduationDate()
                ));

        timelineEventService.createEvent(
                student.getInstitutionId(),
                student.getId(),
                TimelineEventType.STUDENT_REGISTERED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Student registered",
                "Student academic identity created",
                student.getPublicId(),
                TimelineReferenceType.STUDENT,
                null,
                metadata
        );

        eventPublisher.publishCreated(
                new StudentCreatedEvent(
                        student.getPublicId(),
                        student.getInstitutionMembershipId(),
                        LocalDateTime.now()
                )
        );
    }

}