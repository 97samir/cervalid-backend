package com.cervalid.platform.bulk.invitation.mapper.dto;
// rows mapeadas, columnas desconocidas, faltantes

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import lombok.*;

import java.util.List;

@Data
@Builder
public class InvitationMappingResult {

    private List<InvitationBulkRowRequest> rows;
    private List<String> unknownColumns;
    private List<String> missingRequiredColumns;
}
