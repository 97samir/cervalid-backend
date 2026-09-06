package com.cervalid.platform.academic.certificate.service;

import com.cervalid.platform.academic.certificate.entity.Certificate;
import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CertificateDocumentValidationService {

    private final HashService hashService;

    public String validateAndResolveHash(
            MultipartFile file,
            String documentHash,
            String documentUrl) {

        validateDocumentSource(
                file,
                documentHash
        );

        validateDocumentUrl(
                documentUrl
        );

        if (hasFile(file)) {

            validatePdf(file);

            return generateFileHash(file);
        }

        String normalizedHash =
                hashService.normalizeHash(
                        documentHash
                );

        validateHash(
                normalizedHash
        );

        return normalizedHash;
    }

    private void validateDocumentSource(
            MultipartFile file,
            String documentHash) {

        boolean hasFile =
                hasFile(file);

        boolean hasHash =
                documentHash != null
                        && !documentHash.isBlank();

        if (!hasFile && !hasHash) {

            throw new IllegalArgumentException(
                    "Debe proporcionar un archivo PDF o un hash documental."
            );
        }

        if (hasFile && hasHash) {

            throw new IllegalArgumentException(
                    "Debe proporcionar un archivo PDF o un hash documental, pero no ambos."
            );
        }
    }

    private void validatePdf(
            MultipartFile file) {

        String contentType =
                file.getContentType();

        if (!"application/pdf".equalsIgnoreCase(
                contentType)) {

            throw new IllegalArgumentException(
                    "El documento debe ser un archivo PDF."
            );
        }
    }

    private void validateHash(
            String hash) {

        if (!hashService.isValidSha256(hash)) {

            throw new IllegalArgumentException(
                    "El hash del documento debe ser un SHA-256 válido."
            );
        }
    }

    private String generateFileHash(
            MultipartFile file) {

        try {

            return hashService.hashFile(
                    file.getBytes()
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "No fue posible procesar el archivo PDF.",
                    e
            );
        }
    }

    private void validateDocumentUrl(
            String documentUrl) {

        if (documentUrl == null
                || documentUrl.isBlank()) {

            return;
        }

        String normalizedUrl =
                documentUrl.trim();

        if (!normalizedUrl.startsWith("https://")) {

            throw new IllegalArgumentException(
                    "La URL del documento debe utilizar HTTPS."
            );
        }
    }

    public void validateOptionalDocument(
            String documentHash,
            String documentUrl) {

        boolean hasHash =
                documentHash != null
                        && !documentHash.isBlank();

        boolean hasUrl =
                documentUrl != null
                        && !documentUrl.isBlank();

        if (hasHash) {

            String normalizedHash =
                    hashService.normalizeHash(
                            documentHash
                    );

            validateHash(normalizedHash);
        }

        if (hasUrl) {

            validateDocumentUrl(
                    documentUrl
            );
        }
    }

    private boolean hasFile(
            MultipartFile file) {

        return file != null
                && !file.isEmpty();
    }
}