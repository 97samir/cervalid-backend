package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.filter.CredentialFilterRequest;
import com.cervalid.platform.academic.credential.dto.response.CredentialDetailResponse;
import com.cervalid.platform.academic.credential.dto.response.CredentialResponse;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.mapper.CredentialMapper;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.credential.repository.CredentialSpecification;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CredentialQueryService {

    private final CredentialRepository credentialRepository;
    private final StudentRepository studentRepository;
    private final CredentialMapper credentialMapper;

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

        return credentialMapper.toDetailResponse(
                credential,
                student
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

    /*Obtiene los credentials de la institución aplicando filtros*/
    public Page<CredentialResponse> search(
            Long institutionId,
            CredentialFilterRequest filter,
            Pageable pageable
    ) {

        return credentialRepository
                .findAll(
                        CredentialSpecification.withFilters(
                                institutionId,
                                filter
                        ),
                        pageable
                )
                .map(credential -> {

                    Student student = getStudent(
                            institutionId,
                            credential.getStudentId()
                    );

                    return credentialMapper.toResponse(
                            credential,
                            student
                    );
                });
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
                .findById(studentId)
                .filter(student ->
                        institutionId.equals(
                                student.getInstitutionId()
                        )
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El estudiante no pertenece a la institución actual"
                        )
                );
    }
}