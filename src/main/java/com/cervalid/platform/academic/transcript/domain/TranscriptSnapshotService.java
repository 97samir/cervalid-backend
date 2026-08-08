package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.TranscriptSnapshot;
import com.cervalid.platform.academic.transcript.repository.TranscriptSnapshotRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TranscriptSnapshotService {

    private final TranscriptSnapshotRepository repository;
    private final ObjectMapper objectMapper;

    public void saveSnapshot(
            Long transcriptId,
            TranscriptSnapshotDTO snapshot,
            String hash) {

        try {

            String json =
                    objectMapper.writeValueAsString(snapshot);

            repository.save(
                    TranscriptSnapshot.builder()
                            .transcriptId(transcriptId)
                            .snapshotJson(json)
                            .hash(hash)
                            .createdAt(LocalDateTime.now())
                            .build()
            );

        } catch (Exception ex) {
            throw new RuntimeException(
                    "Unable to save transcript snapshot",
                    ex
            );
        }
    }
}