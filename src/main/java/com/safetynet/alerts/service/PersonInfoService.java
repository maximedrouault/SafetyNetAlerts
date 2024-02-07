package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.DataContainer;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.utils.MedicalRecordUtils;
import com.safetynet.alerts.utils.PersonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service for retrieving person information based on first name and last name.
 * This class provides methods to search for persons by their first name and last name and retrieve their information.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class PersonInfoService {

    private final DataReader dataReader;
    private final MedicalRecordUtils medicalRecordUtils;
    private final PersonUtils personUtils;

    /**
     * Retrieves person information based on first name and last name.
     *
     * @param firstName The first name of the person to search for.
     * @param lastName  The last name of the person to search for.
     * @return A list of {@link PersonInfoDTO} representing the information of persons with the specified first name and last name.
     * @throws Exception If an error occurs while fetching the data.
     */
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) throws Exception {
        DataContainer dataContainer = dataReader.dataRead();
        List<Person> filteredPersons = personUtils.getPersonsByFirstNameAndLastName(dataContainer.getPersons(), firstName, lastName);

        if (filteredPersons.isEmpty()) {
            log.error("No Person found for Firstname : '{}' and Lastname : '{}'.", firstName, lastName);
            return Collections.emptyList();
        }

        Map<Person, MedicalRecord> personToMedicalRecordMap = medicalRecordUtils.createPersonToMedicalRecordMap(filteredPersons, dataContainer.getMedicalrecords());

        List<PersonInfoDTO> personInfoDTOS = filteredPersons.stream()
            .map(person -> {
                String birthdate = personToMedicalRecordMap.get(person).getBirthdate();
                int age = medicalRecordUtils.getAge(birthdate);
                return createPersonInfoDTO(person, age, personToMedicalRecordMap.get(person));
            })
            .toList();

        log.info("Person info processed for Firstname : '{}' and Lastname : '{}'.", firstName, lastName);
        return personInfoDTOS;
    }


    private PersonInfoDTO createPersonInfoDTO(Person person, int age, MedicalRecord medicalRecord) {

        return PersonInfoDTO.builder()
                .lastName(person.getLastName())
                .address(person.getAddress())
                .age(age)
                .email(person.getEmail())
                .medications(medicalRecord.getMedications())
                .allergies(medicalRecord.getMedications())
                .build();
    }
}