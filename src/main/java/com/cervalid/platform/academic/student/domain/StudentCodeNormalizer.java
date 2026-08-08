package com.cervalid.platform.academic.student.domain;

import org.springframework.stereotype.Component;

@Component
public class StudentCodeNormalizer {

    public String normalize(String code) {

        if (code == null) {
            return null;
        }

        return code
                .trim()
                .toUpperCase()
                .replace(" ", "")
                .replace("_", "-");
    }
}