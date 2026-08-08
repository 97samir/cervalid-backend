package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.academic.certificate.repository.CertificateRepository;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import com.cervalid.platform.academic.transcript.mapper.TranscriptMapper;
import com.cervalid.platform.academic.transcript.repository.TranscriptRepository;
import com.cervalid.platform.security.context.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscriptQueryService {

    private final TranscriptRepository repository;
    private final CertificateRepository certificateRepository;
    private final SecurityContextService securityContextService;
    private final TranscriptMapper transcriptMapper;

    public Transcript getByPublicId(UUID publicId) {

        Long institutionId =
                securityContextService.getInstitutionId();

        return repository
                .findByPublicIdAndInstitutionId(
                        publicId,
                        institutionId
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Transcript not found"));
    }

    public TranscriptResponse toResponse(
            Transcript transcript) {

        UUID certificatePublicId =
                certificateRepository
                        .findByTranscriptId(
                                transcript.getId())
                        .map(Certificate::getPublicId)
                        .orElse(null);

        return transcriptMapper.toResponse(
                transcript,
                certificatePublicId);
    }

}