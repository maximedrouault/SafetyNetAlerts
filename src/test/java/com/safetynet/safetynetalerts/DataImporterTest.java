package com.safetynet.safetynetalerts;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.model.DataContainer;
import com.safetynet.safetynetalerts.service.DataImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.FileNotFoundException;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DataImporterTest {

    @Mock
    private CustomProperties customProperties;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataImporter dataImporter;


    @Test
    public void dataImport_shouldImportDataSuccessfully() throws Exception {
        when(customProperties.getDataSourceFile()).thenReturn("path/to/fictive/data.json");
        DataContainer mockDataContainer = new DataContainer();
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenReturn(mockDataContainer);

        dataImporter.dataImport();

        assertNotNull(dataImporter.getDataContainer());
    }

    @Test
    public void dataImport_shouldThrowFileNotFoundException_whenFileNotFound() throws Exception {
        when(customProperties.getDataSourceFile()).thenReturn("invalid/path.json");
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenThrow(FileNotFoundException.class);

        assertThrows(FileNotFoundException.class, () -> dataImporter.dataImport());
    }

    @Test
    public void dataImport_shouldThrowJsonParseException_whenMalformedJsonFile() throws Exception {
        when(customProperties.getDataSourceFile()).thenReturn("malformed.json");
        when(objectMapper.readValue(any(File.class), eq(DataContainer.class))).thenThrow(JsonParseException.class);

        assertThrows(JsonParseException.class, () -> dataImporter.dataImport());
    }
}
