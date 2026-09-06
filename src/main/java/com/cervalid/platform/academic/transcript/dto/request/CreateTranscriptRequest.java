package com.cervalid.platform.academic.transcript.dto.request;

import com.cervalid.platform.academic.transcript.enums.AcademicPeriodType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTranscriptRequest {

    @NotNull
    private AcademicPeriodType academicPeriodType;

    //private Long studentId;
    @NotBlank
    private String academicPeriod;

    @Valid
    @NotEmpty
    private List<TranscriptItemRequest> items;

}
