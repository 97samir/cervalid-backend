package com.cervalid.platform.bulk.invitation.mapper;
// convertir columnas dinamicas

import com.cervalid.platform.bulk.invitation.dto.request.InvitationBulkRowRequest;
import com.cervalid.platform.bulk.invitation.mapper.dto.InvitationDynamicRow;
import com.cervalid.platform.bulk.invitation.mapper.dto.InvitationMappingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InvitationColumnMapper {

    private final InvitationColumnAliasRegistry aliasRegistry;

    public InvitationMappingResult map(
            List<InvitationDynamicRow> rows) {

        List<InvitationBulkRowRequest> mappedRows =
                new ArrayList<>();

        Set<String> unknownColumns =
                new HashSet<>();

        Set<String> mappedFields =
                new HashSet<>();

        for (InvitationDynamicRow row : rows) {

            InvitationBulkRowRequest mapped =
                    mapRow(
                            row,
                            unknownColumns,
                            mappedFields
                    );

            mappedRows.add(mapped);
        }

        List<String> missingRequiredColumns =
                validateRequiredColumns(mappedFields);

        return InvitationMappingResult.builder()
                .rows(mappedRows)
                .unknownColumns(
                        unknownColumns.stream().toList()
                )
                .missingRequiredColumns(
                        missingRequiredColumns
                )
                .build();
    }

    private InvitationBulkRowRequest mapRow(
            InvitationDynamicRow row,
            Set<String> unknownColumns,
            Set<String> mappedFields) {

        InvitationBulkRowRequest request =
                new InvitationBulkRowRequest();

        request.setRowNumber(row.getRowNumber());

        for (Map.Entry<String, String> entry :
                row.getValues().entrySet()) {

            String originalColumn =
                    Optional.ofNullable(entry.getKey())
                            .map(this::normalize)
                            .orElse("");

            String value =
                    entry.getValue();

            boolean mapped =
                    tryMapField(
                            request,
                            originalColumn,
                            value,
                            mappedFields
                    );

            if (!mapped) {
                unknownColumns.add(entry.getKey());
            }
        }

        return request;
    }

    private boolean tryMapField(
            InvitationBulkRowRequest request,
            String column,
            String value,
            Set<String> mappedFields) {

        Map<String, List<String>> aliases =
                aliasRegistry.aliases();

        for (Map.Entry<String, List<String>> entry :
                aliases.entrySet()) {

            String internalField =
                    entry.getKey();

            List<String> supportedAliases =
                    entry.getValue();

            boolean matches =
                    supportedAliases.stream()
                            .map(this::normalize)
                            .anyMatch(alias ->
                                    alias.equals(column)
                            );

            if (!matches) {
                continue;
            }

            applyField(
                    request,
                    internalField,
                    value
            );

            mappedFields.add(internalField);

            return true;
        }

        return false;
    }

    private void applyField(
            InvitationBulkRowRequest request,
            String field,
            String value) {

        switch (field) {

            case "email" -> request.setEmail(value);
            case "role" -> request.setRole(value);
        }
    }

    private List<String> validateRequiredColumns(
            Set<String> mappedFields) {

        List<String> missing =
                new ArrayList<>();

        if (!mappedFields.contains("email")) {
            missing.add("email");
        }

        if (!mappedFields.contains("role")) {
            missing.add("role");
        }

        return missing;
    }

    private String normalize(String value) {

        return value == null
                ? ""
                : value
                .trim()
                .toLowerCase();
    }
}
