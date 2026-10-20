package com.cervalid.platform.shared.hashing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class CanonicalHashService {

    private final ObjectMapper mapper;

    public String canonicalize(Map<String, Object> payload) {
        try {
            Map<String, Object> sorted = new TreeMap<>(payload);
            return mapper.writeValueAsString(sorted);
        } catch (Exception e) {
            throw new RuntimeException("Error canonicalizing payload", e);
        }
    }

}