package com.cervalid.platform.academic.transcript.controller;

import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.dto.request.TranscriptItemRequest;
import com.cervalid.platform.academic.transcript.dto.request.UpdateTranscriptItemRequest;
import com.cervalid.platform.academic.transcript.service.TranscriptItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/academic/transcripts")
@RequiredArgsConstructor
public class TranscriptItemController {

    private final TranscriptItemService transcriptItemService;

    @PostMapping("/{transcriptPublicId}/items")
    public TranscriptItemDTO addItem(

            @PathVariable UUID transcriptPublicId,
            @Valid
            @RequestBody TranscriptItemRequest request) {

        return transcriptItemService.addItem(
                transcriptPublicId,
                request);
    }

    @PutMapping("/{transcriptPublicId}/items/{itemPublicId}")
    public TranscriptItemDTO updateItem(

            @PathVariable UUID transcriptPublicId,
            @PathVariable UUID itemPublicId,
            @Valid
            @RequestBody UpdateTranscriptItemRequest request) {

        return transcriptItemService.updateItem(
                transcriptPublicId,
                itemPublicId,
                request);
    }

    @DeleteMapping("/{transcriptPublicId}/items/{itemPublicId}")
    public void deleteItem(

            @PathVariable UUID transcriptPublicId,
            @PathVariable UUID itemPublicId) {

        transcriptItemService.deleteItem(
                transcriptPublicId,
                itemPublicId);
    }
}