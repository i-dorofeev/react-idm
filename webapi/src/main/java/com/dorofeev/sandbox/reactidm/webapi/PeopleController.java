package com.dorofeev.sandbox.reactidm.webapi;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PeopleController {

	private final PeopleRepository peopleRepository;

	@Autowired
	public PeopleController(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@GetMapping("/people")
	public Iterable<Person> getPeople() {
		return peopleRepository.findAll();
	}

	@PutMapping(value = "/people")
	public void addPerson(@RequestBody PersonDTO person) {
		peopleRepository.save(new Person(person.getFirstName(), person.getLastName()));
	}


	@Data
	public static class PersonDTO {
		private String firstName;
		private String lastName;
	}

}
