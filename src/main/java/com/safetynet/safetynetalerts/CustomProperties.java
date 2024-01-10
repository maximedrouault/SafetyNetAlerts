package com.safetynet.safetynetalerts;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "com.safetynet.safetynetalerts")
public class CustomProperties {

    private String dataSourceFile;

}
