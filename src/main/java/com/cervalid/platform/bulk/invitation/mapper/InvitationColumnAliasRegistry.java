package com.cervalid.platform.bulk.invitation.mapper;
// Definir aliases soportados

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class InvitationColumnAliasRegistry {

    public Map<String, List<String>> aliases() {

        Map<String, List<String>> aliases =
                new LinkedHashMap<>();

        aliases.put(
                "email",
                List.of(
                        "email",
                        "correo",
                        "correo institucional",
                        "email institucional",
                        "institutional email"
                )
        );

        aliases.put(
                "role",
                List.of(
                        "role",
                        "rol",
                        "perfil",
                        "tipo usuario",
                        "tipo_usuario"
                )
        );

        return aliases;
    }
}
