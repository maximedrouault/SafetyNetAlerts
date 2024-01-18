package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
public class PersonController {

    private final PersonService personService;

    @GetMapping("/person")
    public ResponseEntity<List<Person>> getPersons() throws Exception {
        return personService.getPersons();
    }

    @DeleteMapping("/person")
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) throws Exception {
        return personService.deletePerson(firstName, lastName);
    }

    @PutMapping("/person")
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) throws Exception {
        return personService.updatePerson(person);
    }

    @PostMapping("/person")
    public ResponseEntity<Person> addPerson(@RequestBody Person person) throws Exception {
        return personService.addPerson(person);
    }
}