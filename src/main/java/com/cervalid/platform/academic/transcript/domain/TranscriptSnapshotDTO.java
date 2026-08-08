package com.cervalid.platform.academic.transcript.domain;

import com.cervalid.platform.academic.transcript.dto.item.TranscriptItemDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptSnapshotDTO {

    private UUID transcriptPublicId;
    private UUID studentPublicId;
    private String academicPeriod;
    private List<TranscriptItemDTO> items;
}
