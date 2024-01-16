package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final DataReader dataReader;

    public ResponseEntity<List<Person>> getPersons() throws Exception {
        List<Person> persons = dataReader.dataRead().getPersons();

        if (persons.isEmpty()) {
            log.error("No Persons found");
            return ResponseEntity.notFound().build();
        } else {
            log.info("Persons successfully retrieved");
            return ResponseEntity.ok(persons);
        }
    }
}