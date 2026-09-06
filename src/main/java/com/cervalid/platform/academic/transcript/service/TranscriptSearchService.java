package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptSearchService {

    private final TranscriptRepository repository;
    private final TranscriptQueryService transcriptQueryService;
    private final SecurityContextService securityContextService;

    public List<TranscriptResponse> getAll() {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByInstitutionId(institutionId)
                .stream()
                .map(transcriptQueryService::toResponse)
                .toList();
    }
}