package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.PartyRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartyRoleDao extends CrudRepository<PartyRole, Integer> {
    List<PartyRole> findByPartyId(Integer partyId);
    PartyRole findByPartyIdAndPartyRoleTypeId (Integer partyId, Integer partyRoleTypeId);
}
