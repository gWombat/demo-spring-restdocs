package fr.gwombat.demospringrestdocs.controllers;

import fr.gwombat.demospringrestdocs.domain.Person;
import fr.gwombat.demospringrestdocs.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private PersonRepository personRepository;

    @GetMapping
    public List<Person> listAll() {
        return personRepository.findAll();
    }

    @GetMapping("/{id}")
    public Person getPersonByID(@PathVariable("id") Long id) {
        return personRepository.findById(id).orElse(null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody @Valid Person person) {
        return personRepository.save(person);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePerson(@PathVariable("id") Long id, @RequestBody Person person) {
        final Person personToUpdate = personRepository.findById(id).orElse(new Person());
        personToUpdate.copyFrom(person);
        personRepository.save(personToUpdate);
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
