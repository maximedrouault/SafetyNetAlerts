package com.safetynet.alerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.alerts.CustomProperties;
import com.safetynet.alerts.model.DataContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * This class provides services for writing data to a JSON data destination file.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataWriter {

    private final ObjectMapper objectMapper;
    private final CustomProperties customProperties;

    /**
     * Writes data from a {@link DataContainer} POJO to a JSON data destination file.
     *
     * @param dataContainer The {@link DataContainer} containing the data to be exported.
     * @throws IOException If an error occurs while writing the data destination file.
     */
    public void dataWrite(DataContainer dataContainer) throws Exception {

        // Export POJO to JSON Data destination file with Serialization via Jackson
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.writeValue(new File(customProperties.getDataSourceFile()), dataContainer);

            log.debug("Data successfully exported and written to file");

        } catch (IOException e) {
            log.error("Error exporting and writing data file : " + e.getMessage());
            throw e;
        }
    }
}