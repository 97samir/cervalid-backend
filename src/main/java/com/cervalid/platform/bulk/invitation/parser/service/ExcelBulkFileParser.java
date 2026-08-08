package com.cervalid.platform.bulk.invitation.parser.service;

import com.cervalid.platform.bulk.invitation.parser.dto.ParsedFileResult;
import com.cervalid.platform.bulk.invitation.parser.dto.ParsedRow;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExcelBulkFileParser implements BulkFileParser {

    @Override
    public ParsedFileResult parse(MultipartFile file) {

        try (Workbook workbook =
                     WorkbookFactory.create(file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> iterator = sheet.iterator();

            if (!iterator.hasNext()) {
                throw new RuntimeException("Excel vacío");
            }

            Row headerRow = iterator.next();

            List<String> headers = new ArrayList<>();

            for (Cell cell : headerRow) {
                headers.add(cell.getStringCellValue().trim());
            }

            List<ParsedRow> rows = new ArrayList<>();

            while (iterator.hasNext()) {

                Row row = iterator.next();

                Map<String, String> values = new HashMap<>();

                for (int i = 0; i < headers.size(); i++) {

                    Cell cell = row.getCell(i);

                    values.put(
                            headers.get(i),
                            getCellValue(cell)
                    );
                }

                rows.add(
                        ParsedRow.builder()
                                .rowNumber(row.getRowNum() + 1)
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
            throw new RuntimeException("Error parseando Excel", e);
        }
    }

    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {

            case STRING ->
                    cell.getStringCellValue();

            case NUMERIC ->
                    String.valueOf(cell.getNumericCellValue());

            case BOOLEAN ->
                    String.valueOf(cell.getBooleanCellValue());

            default -> "";
        };
    }
}