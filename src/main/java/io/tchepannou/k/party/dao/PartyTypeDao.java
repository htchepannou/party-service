package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.PartyType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyTypeDao extends CrudRepository<PartyType, Integer> {
}
