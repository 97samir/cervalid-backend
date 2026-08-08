package com.cervalid.platform.bulk.invitation.parser.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedFileResult {

    private List<String> headers; // HEADERS detectados
    private List<ParsedRow> rows; // ROWS dinámicos
    private Integer totalRows; // metadata
}
