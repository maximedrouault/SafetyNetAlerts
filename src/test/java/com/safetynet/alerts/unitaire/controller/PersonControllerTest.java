package com.safetynet.alerts.unitaire.controller;

import com.safetynet.alerts.controller.PersonController;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void testGetPersons() throws Exception {
        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePerson() throws Exception {
        String firstName = "John";
        String lastName = "Boyd";
        when(personService.deletePerson(firstName, lastName)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/person")
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePerson_NotFound() throws Exception {
        String firstName = "Nonexistent";
        String lastName = "Person";
        when(personService.deletePerson(firstName, lastName)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(delete("/person")
                .param("firstName", firstName)
                .param("lastName", lastName))
                .andExpect(status().isNotFound());
    }
}