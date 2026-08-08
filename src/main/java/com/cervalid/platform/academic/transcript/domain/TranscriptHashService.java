package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.shared.hashing.CanonicalHashService;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TranscriptHashService {

    private final TranscriptItemRepository itemRepository;
    private final CanonicalHashService canonicalHashService;
    private final HashService hashService;

    public String generateTranscriptHash(Long transcriptId) {

        List<TranscriptItem> items =
                itemRepository.findByTranscriptId(transcriptId);

        List<Map<String, Object>> normalizedItems = new ArrayList<>();

        for (TranscriptItem item : items) {
            normalizedItems.add(Map.of(
                    "courseCode", item.getCourseCode(),
                    "credits", item.getCredits(),
                    "grade", item.getGrade()
            ));
        }

        Map<String, Object> payload = Map.of(
                "transcriptId", transcriptId,
                "items", normalizedItems
        );

        String canonicalJson = canonicalHashService.canonicalize(payload);

        return hashService.hashString(canonicalJson);
    }
}