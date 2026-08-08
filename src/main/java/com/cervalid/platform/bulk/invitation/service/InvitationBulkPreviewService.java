package com.cervalid.platform.bulk.invitation.service;

import com.cervalid.platform.bulk.common.dto.ValidationResult;
import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkPreviewResponse;
import com.cervalid.platform.bulk.invitation.dto.response.InvitationBulkPreviewRowResponse;
import com.cervalid.platform.bulk.invitation.mapper.InvitationColumnMapper;
import com.cervalid.platform.bulk.invitation.mapper.dto.InvitationDynamicRow;
import com.cervalid.platform.bulk.invitation.mapper.dto.InvitationMappingResult;
import com.cervalid.platform.bulk.invitation.validator.InvitationValidationPipeline;
import com.cervalid.platform.bulk.invitation.parser.dto.ParsedFileResult;
import com.cervalid.platform.bulk.invitation.parser.dto.ParsedRow;
import com.cervalid.platform.bulk.invitation.parser.service.BulkFileParserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvitationBulkPreviewService {

    private final BulkFileParserFactory parserFactory;
    private final InvitationColumnMapper columnMapper;
    private final InvitationValidationPipeline validationPipeline;

    public InvitationBulkPreviewResponse preview(
            MultipartFile file,
            Long institutionId) {

        // parser file
        ParsedFileResult parsed =
                parserFactory.getParser(file)
                        .parse(file);

        // convertir a dinamic rows
        List<InvitationDynamicRow> dynamicRows =
                parsed.getRows()
                        .stream()
                        .map(parsedRow ->
                                InvitationDynamicRow.builder()
                                    .rowNumber(parsedRow.getRowNumber())
                                    .values(parsedRow.getValues())
                                    .build())
                        .toList();

        // mapear columnas
        InvitationMappingResult mappingResult =
                columnMapper.map(dynamicRows);

        List<InvitationBulkRowRequest> mappedRows =
                mappingResult.getRows();

        // VALIDATION + PREVIEW
        List<InvitationBulkPreviewRowResponse> rows =
                new ArrayList<>();

        int validCount = 0;
        int invalidCount = 0;

        for (InvitationBulkRowRequest row : mappedRows) {

            ValidationResult validation =
                    validationPipeline.validate(
                            mappedRows,
                            row,
                            institutionId
                    );

            if (validation.isValid()) {
                validCount++;
            } else {
                invalidCount++;
            }

            rows.add(

                    InvitationBulkPreviewRowResponse.builder()
                            .rowNumber(row.getRowNumber())
                            .email(row.getEmail())
                            .role(row.getRole())
                            .valid(validation.isValid())
                            .errors(validation.getErrors())
                            .build()
            );
        }

        // BUILD FINAL RESPONSE
        return InvitationBulkPreviewResponse.builder()
                .totalRows(mappedRows.size())
                .validRows(validCount)
                .invalidRows(invalidCount)

                // validation metadata
                .unknownColumns(
                        mappingResult.getUnknownColumns())
                .missingRequiredColumns(
                        mappingResult.getMissingRequiredColumns()
                )
                .rows(rows)
                .build();
    }
}