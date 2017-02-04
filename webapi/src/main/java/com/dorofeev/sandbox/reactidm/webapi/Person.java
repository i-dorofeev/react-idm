package com.dorofeev.sandbox.reactidm.webapi;

import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Generated(value = GenerationTime.INSERT)
	private String id;
	private String firstName;
	private String lastName;

	Person() {

	}

	Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
