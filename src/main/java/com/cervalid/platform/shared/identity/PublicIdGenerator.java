package com.cervalid.platform.shared.identity;
// publics IDs
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PublicIdGenerator {

    public UUID generate() {
        return UUID.randomUUID();
    }
}
