package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TranscriptStatusManager {

    private final TranscriptHashService transcriptHashService;

    public void finalizeTranscript(Transcript transcript) {

        if (transcript.getStatus()
                != TranscriptStatus.DRAFT) {
            throw new RuntimeException(
                    "Only DRAFT can be finalized");
        }

        // GENERA HASH FINAL
        String hash =
                transcriptHashService.generateTranscriptHash(
                        transcript.getId());

        transcript.setTranscriptHash(hash);
        transcript.setStatus(TranscriptStatus.FINALIZED);
    }

    public void invalidateTranscript(Transcript transcript) {

        // cuando se modifica un item
        transcript.setTranscriptHash(null);
        transcript.setStatus(TranscriptStatus.DRAFT);
        transcript.setIssuedAt(null);
    }

    public void issueTranscript(Transcript transcript) {

        if (transcript.getStatus()
                == TranscriptStatus.ISSUED) {
            throw new RuntimeException(
                    "Transcript already issued");
        }

        if (transcript.getStatus()
                != TranscriptStatus.FINALIZED) {
            throw new RuntimeException(
                    "Must be FINALIZED first");
        }

        transcript.setStatus(TranscriptStatus.ISSUED);
        transcript.setIssuedAt(LocalDateTime.now());
    }
}