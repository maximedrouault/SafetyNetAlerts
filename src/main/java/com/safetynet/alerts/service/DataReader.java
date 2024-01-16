package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DataReader {

    private DataContainer dataContainer;
    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;


    public DataContainer dataRead() throws Exception {

        // Import JSON Date source file to POJO with Parsing via Jackson
        try {
            dataContainer = objectMapper.readValue(new File(customProperties.getDataSourceFile()), DataContainer.class);

            log.debug("Data source file read and imported successfully");

        } catch (IOException e) {
            log.error("Error reading and importing data source file: " + e.getMessage(), e);
            throw e;
        }

        return dataContainer;
    }
}