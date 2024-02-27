package com.safetynet.alerts.unitaire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private Person person;
    private String jsonBody;

    @BeforeEach
    public void setUp() throws Exception{
        person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .address("1509 Culver St")
                .city("Culver")
                .zip("97451")
                .phone("841-874-6512")
                .email("jaboyd@email.com")
                .build();

        jsonBody = objectMapper.writeValueAsString(person); // Convert object to JSON
    }


    @Test
    public void deletePerson_whenPersonExists_shouldReturnOk() throws Exception {
        when(personService.deletePerson("John", "Boyd")).thenReturn(true);

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Boyd"))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePerson_whenPersonDoesNotExist_shouldReturnNotFound() throws Exception {
        when(personService.deletePerson("Unknown", "Person")).thenReturn(false);

        mockMvc.perform(delete("/person")
                        .param("firstName", "Unknown")
                        .param("lastName", "Person"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updatePerson_whenPersonExists_shouldReturnOk() throws Exception {
        Optional<Person> updatedPerson = Optional.of(person);
        when(personService.updatePerson(person)).thenReturn(updatedPerson);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePerson_whenPersonDoesNotExist_shouldReturnNotFound() throws Exception {
        Optional<Person> updatedPerson = Optional.empty();
        when(personService.updatePerson(person)).thenReturn(updatedPerson);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addPerson_whenPersonExists_shouldReturnConflict() throws Exception {
        Optional<Person> addedPerson = Optional.empty();
        when(personService.addPerson(person)).thenReturn(addedPerson);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isConflict());
    }

    @Test
    public void addPerson_whenPersonDoesNotExist_shouldReturnOK() throws Exception {
        Optional<Person> addedPerson = Optional.of(person);
        when(personService.addPerson(person)).thenReturn(addedPerson);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }

    @Test
    public void getPersons_whenPersonsDoNotExist_shouldReturnNotFound() throws Exception {
        Optional<List<Person>> persons = Optional.empty();
        when(personService.getPersons()).thenReturn(persons);

        mockMvc.perform(get("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPersons_whenPersonsExist_shouldReturnOK() throws Exception {
        Optional<List<Person>> persons = Optional.of(List.of(person));
        when(personService.getPersons()).thenReturn(persons);

        mockMvc.perform(get("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk());
    }
}