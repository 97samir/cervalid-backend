package com.cervalid.platform.bulk.invitation.parser.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsedRow {

    private Integer rowNumber;
    // HEADER ORIGINAL -> VALUE
    private Map<String, String> values;
}
