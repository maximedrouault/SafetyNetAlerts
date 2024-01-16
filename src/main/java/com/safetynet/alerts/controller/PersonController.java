package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
public class PersonController {

    private final PersonService personService;

    @GetMapping("/person")
    public ResponseEntity<List<Person>> getPersons() throws Exception {
        return personService.getPersons();
    }
}