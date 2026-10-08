package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.academic.credential.entity.Credential;
import com.cervalid.platform.academic.credential.enums.CredentialType;
import com.cervalid.platform.academic.student.entity.Student;
import com.cervalid.platform.academic.timeline.enums.TimelineEventSource;
import com.cervalid.platform.academic.timeline.enums.TimelineEventType;
import com.cervalid.platform.academic.timeline.enums.TimelineReferenceType;
import com.cervalid.platform.academic.timeline.service.TimelineEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialTimelineService {

    private final TimelineEventService timelineEventService;

    public void credentialIssued(
            Credential credential,
            Student student) {

        timelineEventService.createEvent(
                credential.getInstitutionId(),
                student.getId(),
                TimelineEventType.CREDENTIAL_ISSUED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Título académico obtenido",
                buildIssuedDescription(credential),
                credential.getPublicId(),
                TimelineReferenceType.CREDENTIAL,
                null,
                null
        );
    }

    public void credentialRevoked(
            Credential credential,
            Student student) {

        timelineEventService.createEvent(
                credential.getInstitutionId(),
                student.getId(),
                TimelineEventType.CREDENTIAL_REVOKED,
                TimelineEventSource.INSTITUTION_ADMIN,
                "Título académico revocado",
                buildRevokedDescription(credential),
                credential.getPublicId(),
                TimelineReferenceType.CREDENTIAL,
                null,
                null
        );
    }

    private String buildIssuedDescription(
            Credential credential) {

        String typeLabel =
                getCredentialTypeLabel(
                        credential.getType()
                );

        return "Se otorgó oficialmente el "
                + typeLabel
                + " "
                + credential.getTitle()
                + ".";
    }

    private String buildRevokedDescription(
            Credential credential) {

        String typeLabel =
                getCredentialTypeLabel(
                        credential.getType()
                );

        return "El "
                + typeLabel
                + " "
                + credential.getTitle()
                + " fue revocado.";
    }

    private String getCredentialTypeLabel(
            CredentialType type) {

        return switch (type) {

            case BACHELOR_DEGREE ->
                    "grado de bachiller";

            case PROFESSIONAL_TITLE ->
                    "título profesional";

            case MASTER_DEGREE ->
                    "grado de maestro";

            case DOCTORATE ->
                    "grado de doctor";
        };
    }
}