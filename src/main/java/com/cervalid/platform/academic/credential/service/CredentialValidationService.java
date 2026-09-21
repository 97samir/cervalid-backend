package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.dto.request.CreateCredentialRequest;
import com.cervalid.platform.academic.credential.dto.request.UpdateCredentialRequest;
import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialStatus;
import com.cervalid.platform.academic.credential.repository.CredentialRepository;
import com.cervalid.platform.academic.student.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CredentialValidationService {

    private final CredentialRepository credentialRepository;

    /* Valida que el estudiante pueda recibir este tipo de credential */
    public void validateCredentialUniqueness(
            Long institutionId,
            Long studentId,
            com.cervalid.platform.academic.credential.enums.CredentialType type
    ) {
        boolean exists =
                credentialRepository
                        .existsByInstitutionIdAndStudentIdAndTypeAndDeletedFalse(
                                institutionId,
                                studentId,
                                type
                        );

        if (exists) {
            throw new IllegalArgumentException(
                    "El estudiante ya posee un credential de este tipo"
            );
        }
    }

    /* Valida los datos de creación */
    public void validateCreate(
            Long institutionId,
            Student student,
            CreateCredentialRequest request
    ) {
        validateStudentOwnership(
                institutionId,
                student
        );

        validateAwardedDate(
                request.getAwardedAt()
        );

        validateCredentialUniqueness(
                institutionId,
                student.getId(),
                request.getType()
        );
    }

    /* Valida los datos de actualización */
    public void validateUpdate(
            Credential credential,
            UpdateCredentialRequest request
    ) {
        validateNotDeleted(credential);

        if (credential.getStatus() != CredentialStatus.DRAFT) {
            throw new IllegalStateException(
                    "Solo los credentials en estado DRAFT pueden modificarse"
            );
        }
    }

    /* Valida que el credential pueda ser emitido */
    public void validateIssue(
            Credential credential
    ) {
        validateNotDeleted(credential);

        if (credential.getStatus() != CredentialStatus.DRAFT) {
            throw new IllegalStateException(
                    "Solo un credential en estado DRAFT puede ser emitido"
            );
        }

        if (credential.getAwardedAt() == null) {
            throw new IllegalStateException(
                    "El credential no tiene fecha de otorgamiento"
            );
        }
    }

    /* Valida que el credential pueda ser revocado */
    public void validateRevoke(
            Credential credential
    ) {
        validateNotDeleted(credential);

        if (credential.getStatus() != CredentialStatus.ISSUED) {
            throw new IllegalStateException(
                    "Solo un credential emitido puede ser revocado"
            );
        }
    }

    /* Valida que el credential pertenezca a la institución */
    public void validateInstitutionOwnership(
            Credential credential,
            Long institutionId
    ) {
        if (!credential.getInstitutionId().equals(institutionId)) {
            throw new IllegalArgumentException(
                    "El credential no pertenece a la institución actual"
            );
        }
    }

    /* Valida que el estudiante pertenezca a la institución */
    public void validateStudentOwnership(
            Long institutionId,
            Student student
    ) {
        if (!institutionId.equals(student.getInstitutionId())) {
            throw new IllegalArgumentException(
                    "El estudiante no pertenece a la institución actual"
            );
        }
    }

    private void validateAwardedDate(
            LocalDate awardedAt
    ) {
        if (awardedAt == null) {
            throw new IllegalArgumentException(
                    "La fecha de otorgamiento es obligatoria"
            );
        }

        if (awardedAt.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "La fecha de otorgamiento no puede ser futura"
            );
        }
    }

    private void validateNotDeleted(
            Credential credential
    ) {
        if (credential.isDeleted()) {
            throw new IllegalStateException(
                    "El credential no está disponible"
            );
        }
    }

}