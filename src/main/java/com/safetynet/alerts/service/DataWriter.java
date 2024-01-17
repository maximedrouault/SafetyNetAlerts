package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.alerts.CustomProperties;
import com.safetynet.alerts.model.DataContainer;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Data
@Slf4j
public class DataWriter {

    private final CustomProperties customProperties;


    public void dataWrite(DataContainer dataContainer) throws Exception {

        // Export POJO to JSON Data destination file with Serialization via Jackson
        try {
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(customProperties.getDataSourceFile()), dataContainer);

            log.debug("Data successfully exported and written to file");

        } catch (IOException e) {
            log.error("Error exporting and writing data file: " + e.getMessage(), e);
            throw e;
        }
    }
}