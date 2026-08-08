package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.entity.TranscriptItem;
import com.cervalid.platform.academic.transcript.mapper.TranscriptMapper;
import com.cervalid.platform.academic.transcript.repository.TranscriptItemRepository;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptSearchService {

    private final TranscriptRepository repository;
    private final TranscriptMapper mapper;
    private final TranscriptQueryService transcriptQueryService;
    private final SecurityContextService securityContextService;

    public List<TranscriptResponse> getAll() {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findAll()
                .stream()
                .filter(t ->
                        t.getInstitutionId()
                                .equals(institutionId))
                .map(transcriptQueryService::toResponse)
                .toList();
    }
}