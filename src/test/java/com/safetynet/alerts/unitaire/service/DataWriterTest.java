package com.safetynet.alerts.unitaire.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.CustomProperties;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.service.DataWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataWriterTest {

    @Mock
    private CustomProperties customProperties;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataWriter dataWriter;


    @Test
    public void dataWrite_shouldWriteDataSuccessfully_whenValidDataContainerProvided() throws Exception {
        DataContainer mockDataContainer = DataContainer.builder().build();
        when(customProperties.getDataSourceFile()).thenReturn("path/to/fictive/testData.json");

        dataWriter.dataWrite(mockDataContainer);

        verify(objectMapper).writeValue(any(File.class), eq(mockDataContainer));
    }

    @Test
    public void dataWrite_shouldThrowIOException_whenWriteFails() throws Exception {
        DataContainer mockDataContainer = DataContainer.builder().build();
        when(customProperties.getDataSourceFile()).thenReturn("path/to/failure/testData.json");
        doThrow(IOException.class).when(objectMapper).writeValue(any(File.class), eq(mockDataContainer));

        assertThrows(IOException.class, () -> dataWriter.dataWrite(mockDataContainer));
    }
}