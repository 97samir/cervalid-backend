package com.cervalid.platform.academic.shared.validation;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscriptOwnershipValidator {

    private final TranscriptRepository repository;

    public Transcript validateOwnership(
            UUID transcriptPublicId,
            Long institutionId) {

        System.out.println("VALIDATE transcriptId = " + transcriptPublicId);
        System.out.println("VALIDATE institutionId = " + institutionId);

        System.out.println("CHECK DB transcriptId = " + transcriptPublicId);

        Transcript transcript = repository
                .findByPublicIdAndInstitutionId(transcriptPublicId, institutionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transcript not found for institution=" + institutionId
                        ));

        if (!transcript.getInstitutionId()
                .equals(institutionId)) {

            throw new RuntimeException(
                    "Transcript does not belong to institution");
        }

        if (transcript.getStatus() != TranscriptStatus.FINALIZED) {
            throw new RuntimeException
                    ("Transcript is not finalized");
        }

        return transcript;
    }
}
