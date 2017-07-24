package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDao extends CrudRepository<Person, Integer> {
}
