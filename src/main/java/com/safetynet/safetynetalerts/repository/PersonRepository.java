package com.safetynet.safetynetalerts.repository;

import com.safetynet.safetynetalerts.model.DataContainer;
import com.safetynet.safetynetalerts.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

}