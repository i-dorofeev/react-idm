package com.dorofeev.sandbox.reactidm.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class InitializationBean {

	private final PeopleRepository peopleRepository;

	@Autowired
	public InitializationBean(PeopleRepository peopleRepository) {
		this.peopleRepository = peopleRepository;
	}

	@PostConstruct
	public void initialize() {
		peopleRepository.save(new Person("1", "Ilya", "Dorofeev"));
		peopleRepository.save(new Person("2", "Boris", "Romanov"));
		peopleRepository.save(new Person("3", "Nickolay", "Romanov"));

	}
}
