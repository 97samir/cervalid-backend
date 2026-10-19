package com.cervalid.platform.demo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cervalid.demo")
public class DemoDataProperties {

    private boolean enabled = false;
    private String password = "";

}