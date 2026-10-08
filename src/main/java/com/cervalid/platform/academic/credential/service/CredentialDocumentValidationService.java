package com.cervalid.platform.academic.credential.service;

import com.cervalid.platform.shared.hashing.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CredentialDocumentValidationService {

    private static final String SHA256_REGEX =
            "^0x[a-fA-F0-9]{64}$";

    private final HashService hashService;

    public String validateAndResolveHash(
            MultipartFile file,
            String documentHash,
            String documentUrl
    ) {

        boolean hasFile =
                file != null && !file.isEmpty();

        boolean hasHash =
                documentHash != null
                        && !documentHash.isBlank();

        // No se está actualizando el hash.
        // Esto permite actualizar solamente la URL.
        if (!hasFile && !hasHash) {
            return null;
        }

        if (hasHash) {

            String normalizedHash =
                    hashService.normalizeHash(documentHash);

            if (!normalizedHash.matches(SHA256_REGEX)) {
                throw new IllegalArgumentException(
                        "El hash debe ser un SHA-256 válido de 64 caracteres hexadecimales."
                );
            }

            documentHash = normalizedHash;
        }

        // Si se proporciona un PDF,
        // el hash se genera automáticamente.
        if (hasFile) {

            validatePdf(file);

            String generatedHash =
                    generateSha256(file);

            // Si se proporcionó un hash manual,
            // comprobamos que corresponda al PDF.
            if (hasHash) {

                if (!generatedHash.equals(documentHash)) {
                    throw new IllegalArgumentException(
                            "El hash proporcionado no coincide con el documento."
                    );
                }
            }

            return generatedHash;
        }

        // Solo se proporcionó un hash.
        return documentHash;
    }

    private void validatePdf(
            MultipartFile file
    ) {

        String contentType =
                file.getContentType();

        if (contentType == null
                || !contentType.equalsIgnoreCase(
                "application/pdf")) {

            throw new IllegalArgumentException(
                    "El documento debe ser un archivo PDF."
            );
        }
    }

    private String generateSha256(
            MultipartFile file
    ) {

        try {

            return hashService.hashFile(
                    file.getBytes()
            );

        } catch (Exception e) {

            throw new IllegalStateException(
                    "No fue posible generar el hash del documento.",
                    e
            );
        }
    }
}