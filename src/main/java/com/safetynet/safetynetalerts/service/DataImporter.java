package com.safetynet.safetynetalerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.CustomProperties;
import com.safetynet.safetynetalerts.model.DataContainer;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class DataImporter {

    private DataContainer dataContainer;
    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;


    @PostConstruct
    public void dataImport() throws Exception {

        // Import JSON Date source file to POJO with Parsing via Jackson
        try {
            dataContainer = objectMapper.readValue(new File(customProperties.getDataSourceFile()), DataContainer.class);

            log.info("Data source file successfully imported");

        } catch (IOException e) {
            log.error("Error importing Data source file");
            throw e;
        }
    }
}