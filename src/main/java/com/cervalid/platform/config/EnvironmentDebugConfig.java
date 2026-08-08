package com.cervalid.platform.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvironmentDebugConfig {

    @Value("${app.env-name:NOT_DEFINED}")
    private String envName;

    @PostConstruct
    public void printEnv() {
        System.out.println("====================================");
        System.out.println("Entorno cargado desde YAML: " + envName);
        System.out.println("====================================");
    }
}
