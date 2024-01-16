package com.safetynet.alerts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "com.safetynet.safetynetalerts")
public class CustomProperties {

    private String dataSourceFile;

}