package com.dorofeev.sandbox.reactidm.webapi;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PeopleController {

	private final PeopleRepository peopleRepository;
	private final PeopleServiceAPI peopleServiceAPI;

	@Autowired
	public PeopleController(PeopleRepository peopleRepository, PeopleServiceAPI peopleServiceAPI) {
		this.peopleRepository = peopleRepository;
		this.peopleServiceAPI = peopleServiceAPI;
	}

	@GetMapping("/people")
	public Iterable<Person> getPeople() {
		return peopleRepository.findAll();
	}

	@PutMapping(value = "/people")
	public CompletionStage<Void> addPerson(@RequestBody PersonDTO person) {
		peopleRepository.save(new Person(person.getFirstName(), person.getLastName()));
		return peopleServiceAPI.createNewPerson(person.getFirstName(), person.getLastName());
	}

	@Data
	private static class PersonDTO {
		private String firstName;
		private String lastName;
	}

}
