package com.dorofeev.sandbox.reactidm.webapi;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Person {

	@Id
	private String id;
	private String firstName;
	private String lastName;

	Person() {

	}

	Person(String id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
