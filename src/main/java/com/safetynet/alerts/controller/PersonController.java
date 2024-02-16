package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Tag(name = "API Controller pour les opérations CRUD sur les Persons")
@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    // CRUD

    @DeleteMapping("/person")
    @Operation(description = "Permet de supprimer une personne")
    public ResponseEntity<Void> deletePerson(@RequestBody Person personToDelete) throws Exception {
        return personService.deletePerson(personToDelete) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/person")
    @Operation(description = "Permet de mettre à jour une personne")
    public ResponseEntity<Person> updatePerson(@RequestBody Person personToUpdate) throws Exception {
        Optional<Person> updatedPerson = personService.updatePerson(personToUpdate);
        return updatedPerson
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/person")
    @Operation(description = "Permet d'ajouter une personne")
    public ResponseEntity<Person> addPerson(@RequestBody Person personToAdd) throws Exception {
        Optional<Person> addedPerson = personService.addPerson(personToAdd);
        return addedPerson
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }
}