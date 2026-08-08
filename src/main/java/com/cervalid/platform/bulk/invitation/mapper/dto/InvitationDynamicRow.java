package com.cervalid.platform.bulk.invitation.mapper.dto;
// fila dinámica proveniente del Excel

import lombok.*;

import java.util.Map;

@Data
@Builder
public class InvitationDynamicRow {

    private Integer rowNumber;
    // columna_original -> valor
    private Map<String, String> values;
}
