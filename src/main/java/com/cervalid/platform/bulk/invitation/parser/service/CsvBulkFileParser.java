package com.cervalid.platform.bulk.invitation.parser.service;

import com.cervalid.platform.bulk.invitation.parser.dto.ParsedFileResult;
import com.cervalid.platform.bulk.invitation.parser.dto.ParsedRow;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvBulkFileParser implements BulkFileParser {

    @Override
    public ParsedFileResult parse(MultipartFile file) {

        try {

            CSVReader reader = new CSVReader(
                    new InputStreamReader(file.getInputStream())
            );

            List<String[]> lines = reader.readAll();

            if (lines.isEmpty()) {
                throw new RuntimeException("CSV vacío");
            }

            List<String> headers =
                    Arrays.stream(lines.get(0))
                            .map(String::trim)
                            .toList();

            List<ParsedRow> rows = new ArrayList<>();

            for (int i = 1; i < lines.size(); i++) {

                String[] line = lines.get(i);

                Map<String, String> values = new HashMap<>();

                for (int j = 0; j < headers.size(); j++) {

                    String value =
                            j < line.length
                                    ? line[j]
                                    : "";

                    values.put(
                            headers.get(j),
                            value != null ? value.trim() : ""
                    );
                }

                rows.add(
                        ParsedRow.builder()
                                .rowNumber(i + 1)
                                .values(values)
                                .build()
                );
            }

            return ParsedFileResult.builder()
                    .headers(headers)
                    .rows(rows)
                    .totalRows(rows.size())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error parseando CSV", e);
        }
    }
}
