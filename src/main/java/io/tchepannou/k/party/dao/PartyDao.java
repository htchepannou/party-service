package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.Party;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyDao extends CrudRepository<Party, Integer> {
}
