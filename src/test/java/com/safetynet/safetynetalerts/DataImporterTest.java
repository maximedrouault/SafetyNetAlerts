package com.safetynet.safetynetalerts;

import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import com.safetynet.safetynetalerts.service.DataImporter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataImporterTest {

    @Mock
    private CustomProperties customProperties;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private FireStationRepository fireStationRepository;
    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private Logger logger;

    @InjectMocks
    private DataImporter dataImporter;

    @Test
    public void dataImport_ShouldLogError_WhenFileNotFound() {
        when(customProperties.getDataSourceFile()).thenReturn("");

        dataImporter.dataImport();

        verify(personRepository, times(0)).saveAll((any()));
        verify(fireStationRepository, times(0)).saveAll((any()));
        verify(medicalRecordRepository, times(0)).saveAll((any()));

        verify(logger).error(eq("Error importing Data source file to DB"), any(IOException.class));
    }

    @Test
    public void dataImport_ShouldSaveEntitiesToDb_WhenDataSourceFileIsProvided() {
        when(customProperties.getDataSourceFile()).thenReturn("src/test/resources/testData.json");

        dataImporter.dataImport();

        verify(personRepository, times(1)).saveAll(any());
        verify(fireStationRepository, times(1)).saveAll(any());
        verify(medicalRecordRepository, times(1)).saveAll(any());

        verify(logger).info(eq("Data source file successfully imported into DB"));
    }
}