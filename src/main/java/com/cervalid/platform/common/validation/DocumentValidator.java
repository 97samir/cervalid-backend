package com.cervalid.platform.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentValidator implements ConstraintValidator<DocumentValid, DocumentHolder> {

    @Override
    public boolean isValid(DocumentHolder req, ConstraintValidatorContext context) {

        if (req == null) return true; // evitar null pointer en validación global

        if (req.getDocumentType() == null || req.getDocument() == null) {
            return false;
        }

        String document = req.getDocument();

        switch (req.getDocumentType()) {

            case DNI:
                return document.matches("\\d{8}");

            case CE:
                return document.matches("\\d{9}");

            default:
                return false;
        }
    }
}