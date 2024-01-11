package com.safetynet.safetynetalerts.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.safetynetalerts.CustomProperties;
import com.safetynet.safetynetalerts.model.DataContainer;
import com.safetynet.safetynetalerts.repository.FireStationRepository;
import com.safetynet.safetynetalerts.repository.MedicalRecordRepository;
import com.safetynet.safetynetalerts.repository.PersonRepository;
import jakarta.annotation.PostConstruct;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Setter
@Service
public class DataImporter {

    private Logger logger = LoggerFactory.getLogger(DataImporter.class);
    @Autowired
    private CustomProperties customProperties;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private FireStationRepository fireStationRepository;
    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @PostConstruct
    public void dataImport() {

        // Import JSON Date source file to POJO with Parsing via Jackson
        ObjectMapper mapper = new ObjectMapper();
        try {
            DataContainer dataSource = mapper.readValue(new File(customProperties.getDataSourceFile()), DataContainer.class);

            // Save POJO to DB
            personRepository.saveAll(dataSource.getPersons());
            fireStationRepository.saveAll((dataSource.getFirestations()));
            medicalRecordRepository.saveAll((dataSource.getMedicalrecords()));

            logger.info("Data source file successfully imported into DB");

        } catch (IOException e) {
            logger.error("Error importing Data source file to DB", e);
        }
    }
}