package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.dto.response.CredentialDetailResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialVerificationResponse;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.mapper.CredentialMapper;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.credential.repository.CredentialSpecification;
import com.cervalid.platform.academic.profile.entity.AcademicProfile;
import com.cervalid.platform.academic.profile.repository.AcademicProfileRepository;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import com.cervalid.platform.institution.entity.Institution;
import com.cervalid.platform.institution.repository.InstitutionRepository;
import com.cervalid.platform.user.entity.User;
import com.cervalid.platform.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialQueryService {

    private final CredentialRepository credentialRepository;
    private final StudentRepository studentRepository;
    private final CredentialMapper credentialMapper;

    private final AcademicProfileRepository academicProfileRepository;
    private final UserRepository userRepository;
    private final InstitutionRepository institutionRepository;

    /*Obtiene un credential por su publicId dentro de la institución actual*/
    public CredentialDetailResponse getByPublicId(
            Long institutionId,
            UUID publicId
    ) {

        Credential credential = credentialRepository
                .findByPublicIdAndInstitutionIdAndDeletedFalse(
                        publicId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El credential no existe"
                        )
                );

        Student student = getStudent(
                institutionId,
                credential.getStudentId()
        );

        User user = getUser(student);

        AcademicProfile academicProfile = getAcademicProfile(
                institutionId,
                student.getId()
        );

        Institution institution = getInstitution(institutionId);

        return credentialMapper.toDetailResponse(
                credential,
                student,
                user,
                academicProfile,
                institution
        );
    }

    /*Obtiene todos los credentials de un estudiante*/
    public Page<CredentialResponse> getByStudent(
            Long institutionId,
            UUID studentPublicId,
            Pageable pageable
    ) {

        Student student = studentRepository
                .findByPublicIdAndInstitutionId(
                        studentPublicId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El estudiante no existe en la institución actual"
                        )
                );

        return credentialRepository
                .findAllByInstitutionIdAndStudentIdAndDeletedFalse(
                        institutionId,
                        student.getId(),
                        pageable
                )
                .map(credential ->
                        credentialMapper.toResponse(
                                credential,
                                student
                        )
                );
    }

    //metodos de obtencion
    private User getUser(
            Student student) {

        return userRepository
                .findById(student.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El usuario asociado al estudiante no existe"
                        )
                );
    }

    private AcademicProfile getAcademicProfile(
            Long institutionId,
            Long studentId
    ) {

        return academicProfileRepository
                .findByStudentIdAndInstitutionId(
                        studentId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El perfil académico del estudiante no existe"
                        )
                );
    }

    private Institution getInstitution(
            Long institutionId
    ) {

        return institutionRepository
                .findById(institutionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La institución no existe"
                        )
                );
    }

    /*Obtiene los credentials de la institución aplicando filtros*/
    public Page<CredentialResponse> search(
            Long institutionId,
            CredentialFilterRequest filter,
            Pageable pageable
    ) {

        Long studentId = null;

        if (filter != null
                && filter.getStudentPublicId() != null) {

            Student student =
                    studentRepository
                            .findByPublicIdAndInstitutionId(
                                    filter.getStudentPublicId(),
                                    institutionId
                            )
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "El estudiante no existe en la institución actual"
                                    )
                            );

            studentId = student.getId();
        }

        Page<Credential> credentials =
                credentialRepository.findAll(
                        CredentialSpecification.withFilters(
                                institutionId,
                                filter,
                                studentId
                        ),
                        pageable
                );

        Set<Long> studentIds =
                credentials.getContent()
                        .stream()
                        .map(Credential::getStudentId)
                        .collect(Collectors.toSet());

        Map<Long, Student> students =
                studentRepository
                        .findAllByInstitutionIdAndIdIn(
                                institutionId,
                                studentIds
                        )
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        Student::getId,
                                        student -> student
                                )
                        );

        return credentials.map(credential ->
                credentialMapper.toResponse(
                        credential,
                        students.get(
                                credential.getStudentId()
                        )
                )
        );
    }

    /* Obtiene un credencial interno.
    cuando otra capa del módulo necesita trabajar directamente
    con la entidad*/
    public Credential getEntity(
            Long institutionId,
            UUID publicId
    ) {

        return credentialRepository
                .findByPublicIdAndInstitutionIdAndDeletedFalse(
                        publicId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El credential no existe"
                        )
                );
    }

    private Student getStudent(
            Long institutionId,
            Long studentId
    ) {

        return studentRepository
                .findByIdAndInstitutionId(
                        studentId,
                        institutionId
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El estudiante no pertenece a la institución actual"
                        )
                );
    }

    public CredentialVerificationResponse getVerificationByPublicId(
            UUID publicId
    ) {

        Credential credential =
                credentialRepository
                        .findByPublicIdAndDeletedFalse(publicId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El credential no existe"
                                )
                        );

        if (credential.getStatus() == CredentialStatus.DRAFT) {
            throw new IllegalStateException(
                    "El credential todavía no ha sido emitido"
            );
        }

        Student student =
                studentRepository
                        .findById(credential.getStudentId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El estudiante asociado al credential no existe"
                                )
                        );

        User user = getUser(student);

        AcademicProfile academicProfile =
                academicProfileRepository
                        .findByStudentIdAndInstitutionId(
                                student.getId(),
                                credential.getInstitutionId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "El perfil académico del estudiante no existe"
                                )
                        );

        Institution institution =
                getInstitution(credential.getInstitutionId());

        return credentialMapper.toVerificationResponse(
                credential,
                student,
                user,
                academicProfile,
                institution
        );
    }
}