package com.dorofeev.sandbox.reactidm.webapi;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepository extends CrudRepository<Person, String> {
}
