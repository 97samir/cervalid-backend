package com.cervalid.platform.academic.transcript.service;
import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import com.cervalid.platform.academic.transcript.dto.request.TranscriptItemRequest;
import com.cervalid.platform.academic.transcript.dto.request.UpdateTranscriptItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TranscriptItemService {

    private final TranscriptItemManagementService managementService;

    public TranscriptItemDTO addItem(
            UUID transcriptPublicId,
            TranscriptItemRequest request) {

        return managementService.addItem(
                transcriptPublicId,
                request);
    }

    public TranscriptItemDTO updateItem(
            UUID transcriptPublicId,
            UUID itemPublicId,
            UpdateTranscriptItemRequest request) {

        return managementService.updateItem(
                transcriptPublicId,
                itemPublicId,
                request);
    }

    public void deleteItem(
            UUID transcriptPublicId,
            UUID itemPublicId) {

        managementService.deleteItem(
                transcriptPublicId,
                itemPublicId);
    }

}