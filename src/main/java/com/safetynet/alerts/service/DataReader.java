package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.config.CustomProperties;
import com.safetynet.alerts.model.DataContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * This class provides services for reading data from a JSON data source file and mapping it to a POJO.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class DataReader {

    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;

    /**
     * Reads data from a JSON data source file and maps it to a {@link DataContainer} POJO.
     *
     * @return The {@link DataContainer} containing the imported data.
     * @throws IOException If an error occurs while reading the data source file.
     */
    public DataContainer dataRead() throws Exception {
        DataContainer dataContainer;

        // Import JSON Data source file to POJO with Parsing via Jackson
        try {
            dataContainer = objectMapper.readValue(new File(customProperties.getDataSourceFile()), DataContainer.class);

            log.debug("Data file read and imported successfully");

        } catch (IOException e) {
            log.error("Error reading and importing data source file : " + e.getMessage());
            throw e;
        }

        return dataContainer;
    }
}