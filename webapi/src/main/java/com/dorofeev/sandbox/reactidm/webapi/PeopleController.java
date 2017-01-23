package com.dorofeev.sandbox.reactidm.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
