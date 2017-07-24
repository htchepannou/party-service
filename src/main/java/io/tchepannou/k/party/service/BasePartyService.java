package io.tchepannou.k.party.service;

import io.tchepannou.k.party.dao.PartyDao;
import io.tchepannou.k.party.dao.PartyTypeDao;
import io.tchepannou.k.party.domain.Party;
import org.springframework.beans.factory.annotation.Autowired;

public class BasePartyService {
    @Autowired
    protected PartyTypeDao partyTypeDao;

    @Autowired
    protected PartyDao partyDao;

    protected final Party createParty(Integer partyTypeId) {
        final Party party = new Party();
        party.setPartyTypeId(partyTypeId);
        partyDao.save(party);

        return party;
    }

}
