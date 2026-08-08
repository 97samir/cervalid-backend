package com.cervalid.platform.academic.transcript.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTranscriptRequest {

    //private Long studentId;
    @NotBlank
    private String academicPeriod;

    @Valid
    @NotEmpty
    private List<TranscriptItemRequest> items;

}
