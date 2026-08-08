package com.cervalid.platform.bulk.invitation.parser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BulkFileParserFactory {

    private final CsvBulkFileParser csvParser;

    private final ExcelBulkFileParser excelParser;

    public BulkFileParser getParser(MultipartFile file) {

        String filename = file.getOriginalFilename();

        if (filename == null) {
            throw new RuntimeException("Archivo inválido");}

        if (filename.endsWith(".csv")) {
            return csvParser;}

        if (filename.endsWith(".xlsx")) {
            return excelParser;}

        throw new RuntimeException(
                "Formato no soportado"
        );
    }
}