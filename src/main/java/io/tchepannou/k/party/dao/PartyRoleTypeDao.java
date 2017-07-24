package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.PartyRoleType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyRoleTypeDao extends CrudRepository<PartyRoleType, Integer> {
    PartyRoleType findByName(String name);
}
