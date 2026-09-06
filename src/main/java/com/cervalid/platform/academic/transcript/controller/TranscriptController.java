package com.cervalid.platform.academic.transcript.controller;
// TRANSCRIPT - HISTORIAL ACADEMICO
import com.cervalid.platform.academic.transcript.dto.request.CreateTranscriptRequest;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptDetailResponse;
import com.cervalid.platform.academic.transcript.dto.response.TranscriptResponse;
import com.cervalid.platform.academic.transcript.service.TranscriptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/academic/transcripts")
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptService transcriptService;

    // crear
    @PostMapping("/students/{studentPublicId}")
    public TranscriptResponse create(
            @PathVariable UUID studentPublicId,
            @Valid @RequestBody CreateTranscriptRequest request) {

        return transcriptService.create(
                studentPublicId,
                request);
    }

    // cambia estado de draf(modificable) a finalized
    @PostMapping("/{transcriptPublicId}/finalize")
    public ResponseEntity<Void> finalizeTranscript(
            @PathVariable UUID transcriptPublicId) {

        transcriptService.finalizeTranscript(
                transcriptPublicId);

        return ResponseEntity.ok().build();
    }

    // transcript emitido
    @PostMapping("/{transcriptPublicId}/issue")
    public ResponseEntity<Void> issueTranscript(
            @PathVariable UUID transcriptPublicId) {

        transcriptService.issueTranscript(
                transcriptPublicId);

        return ResponseEntity.ok().build();
    }

    // ovtiene transcript
    @GetMapping("/{transcriptPublicId}")
    public TranscriptDetailResponse get(
            @PathVariable UUID transcriptPublicId) {

        return transcriptService.getDetail(
                transcriptPublicId);
    }

    @GetMapping("/students/{studentPublicId}")
    public List<TranscriptResponse> getByStudent(
            @PathVariable UUID studentPublicId) {

        return transcriptService.getByStudent(
                studentPublicId);
    }

    // listado de todos los transcript
    @GetMapping
    public List<TranscriptResponse> getAll() {
        return transcriptService.getAll();
    }

}