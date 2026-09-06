package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.enums.TranscriptStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TranscriptStatusManager {

    public void finalizeTranscript(
            Transcript transcript,
            String hash) {

        if (transcript.getStatus()
                != TranscriptStatus.DRAFT) {

            throw new RuntimeException(
                    "Only DRAFT transcript can be finalized");
        }

        transcript.setTranscriptHash(hash);
        transcript.setStatus(
                TranscriptStatus.FINALIZED);
    }

    public void invalidateTranscript(
            Transcript transcript) {

        if (transcript.getStatus()
                != TranscriptStatus.DRAFT) {

            throw new RuntimeException(
                    "Only DRAFT transcript can be invalidated");
        }

        transcript.setTranscriptHash(null);
        transcript.setIssuedAt(null);
    }

    public void issueTranscript(
            Transcript transcript) {

        if (transcript.getStatus()
                != TranscriptStatus.FINALIZED) {

            throw new RuntimeException(
                    "Only FINALIZED transcript can be issued");
        }

        transcript.setStatus(
                TranscriptStatus.ISSUED);

        transcript.setIssuedAt(
                LocalDateTime.now());
    }
}