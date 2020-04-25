package io.interactive.server.web.rest.controllers;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.interactive.server.domain.Account;
import io.interactive.server.domain.Person;
import io.interactive.server.repository.IPersonRepository;
import io.interactive.server.web.rest.errors.BadRequestAlertException;
import lombok.var;
import org.aspectj.weaver.patterns.PerObject;
import org.hibernate.mapping.Array;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.Query;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Predicate;

@Transactional
@RestController
@RequestMapping("/v1")

public class PersonController {

    private final Logger log = LoggerFactory.getLogger(PersonController.class);

    private static final String ENTITY_NAME = "Person";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IPersonRepository _personRepository;

    @Autowired
    public PersonController(IPersonRepository personRepository){
        _personRepository = personRepository;
    }

    @PostMapping("/persons")
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) throws URISyntaxException {
        log.debug("REST request to save Person : {}", person);
        if (person.getCode() != null) {
            throw new BadRequestAlertException("A new person cannot already have a person code", ENTITY_NAME, "person exists");
        }
        Person result = _personRepository.save(person);
        return ResponseEntity.created(new URI("/v1/persons/" + result.getCode()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCode().toString()))
            .body(result);
    }

    @PutMapping("/persons")
    public ResponseEntity<Person> updatePerson(@Valid @RequestBody Person person) throws URISyntaxException {
        if (person.getCode() == null){
            throw new BadRequestAlertException("Invalid Person Code : {}", ENTITY_NAME, "person does not exist");
        }
        Person result = _personRepository.save(person);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, person.getCode().toString()))
            .body(result);
    }

    @GetMapping("/persons")
    public List<Person> getAllPersons() {
        log.debug("REST request to get all Persons");
        return _personRepository.findAll();
    }

    @GetMapping("/persons/{idNumber}")
    public ResponseEntity<Person> getPerson(@PathVariable String idNumber){
        log.debug("REST request to get Person : {}", idNumber);
        Optional<Person> person = _personRepository.findByIdNumber(idNumber);
        return ResponseUtil.wrapOrNotFound(person);
    }

    @GetMapping("/persons/name")
    public List<Person> getPersonByName(String name){
        log.debug("REST request to get Person by their name");
        return _personRepository.findByName(name);
    }

    @GetMapping("/persons/s_name")
    public List<Person> getPersonSurname(String surname) {
        log.debug("REST request to get Person by their surname");
        return _personRepository.findBySurname(surname);
    }

    @GetMapping("/persons/acc_key")
    public List<Person> getPersonAccountNumber(String account_number) {
        log.debug("REST request to get Person by their account_number");
        return _personRepository.findPersonByAccountNumber(account_number);
    }

    @DeleteMapping("/delete-person/{code}")
    public ResponseEntity<Void> deletePerson(@PathVariable Integer code) {
        log.debug("REST request to delete Person with ZERO debt");

        boolean validAccount = _personRepository.getOne(code).getAccountByCode().removeIf(account ->
            account.getOutstandingBalance().signum() == 0);

        if (validAccount) {
             _personRepository.deleteById(code);
        }
        else {
            throw new BadRequestAlertException(ENTITY_NAME, "Your account is owing money", "YOU ARE IN DEBT");
        }

        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, code.toString())).build();
    }
}
