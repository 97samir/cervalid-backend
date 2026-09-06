package com.cervalid.platform.academic.transcript.service;

import com.cervalid.platform.academic.transcript.dto.request.CreateTranscriptRequest;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptDetailResponse;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.entity.Transcript;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranscriptService {

    private final TranscriptManagementService managementService;
    private final TranscriptDetailQueryService detailQueryService;
    private final TranscriptSearchService searchService;
    private final TranscriptQueryService transcriptQueryService;

    public TranscriptResponse create(
            UUID studentPublicId,
            CreateTranscriptRequest request) {

        Transcript transcript =
                managementService.create(
                        studentPublicId,
                        request);

        //return transcriptMapper.toResponse(transcript);
        return transcriptQueryService.toResponse(
                transcript);
    }

    public void finalizeTranscript(
            UUID transcriptPublicId) {

        managementService.finalizeTranscript(
                transcriptPublicId);
    }

    public void issueTranscript(
            UUID transcriptPublicId) {

        managementService.issueTranscript(
                transcriptPublicId);
    }

    public TranscriptDetailResponse getDetail(
            UUID transcriptPublicId) {

        return detailQueryService.getDetail(
                transcriptPublicId);
    }

    public List<TranscriptResponse> getByStudent(
            UUID studentPublicId) {

        return detailQueryService.getByStudent(
                studentPublicId);
    }

    public List<TranscriptResponse> getAll() {

        return searchService.getAll();
    }

    public Transcript getEntityByPublicId(
            UUID publicId) {

        return transcriptQueryService.getByPublicId(publicId);
    }
}
