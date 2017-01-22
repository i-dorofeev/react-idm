package com.dorofeev.sandbox.reactidm.webapi;

import lombok.Data;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PeopleController {

	@Data
	public static class Person {

		private String id;
		private String firstName;
		private String lastName;

		Person(String id, String firstName, String lastName) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
		}
	}

	@GetMapping("/people")
	public Person[] getPeople() {
		return new Person[] {
			new Person("1", "Ilya", "Dorofeev"),
			new Person("2", "Boris", "Romanov")
		};
	}

}
