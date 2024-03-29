package com.safetynet.alerts.unitaire.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.config.CustomProperties;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.service.DataReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataReaderTest {

    @Mock
    private CustomProperties customProperties;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataReader dataReader;


    @Test
    public void dataRead_shouldImportDataSuccessfully_whenJsonDataFileIsProvided() throws Exception {
        DataContainer dataContainer = DataContainer.builder().build();
        when(customProperties.getDataSourceFile()).thenReturn("path/to/fictive/testData.json");
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenReturn(dataContainer);

        dataReader.dataRead();

        assertNotNull(dataReader.dataRead());
    }

    @Test
    public void dataRead_shouldThrowFileNotFoundException_whenFileNotFound() throws Exception {
        when(customProperties.getDataSourceFile()).thenReturn("invalid/path.json");
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenThrow(FileNotFoundException.class);

        assertThrows(FileNotFoundException.class, () -> dataReader.dataRead());
    }

    @Test
    public void dataRead_shouldThrowJsonParseException_whenMalformedJsonFile() throws Exception {
        when(customProperties.getDataSourceFile()).thenReturn("malformed.json");
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenThrow(JsonParseException.class);

        assertThrows(JsonParseException.class, () -> dataReader.dataRead());
    }
}
