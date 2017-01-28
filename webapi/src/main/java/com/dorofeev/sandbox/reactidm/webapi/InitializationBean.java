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
		/*

		Random random = new Random();
		peopleRepository.save(new Person(Integer.toString(random.nextInt()), "Ilya", "Dorofeev"));
		peopleRepository.save(new Person(Integer.toString(random.nextInt()), "Boris", "Romanov"));
		peopleRepository.save(new Person(Integer.toString(random.nextInt()), "Nickolay", "Romanov"));
		*/
	}
}
