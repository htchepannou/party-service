package io.tchepannou.k.party.service;

import io.tchepannou.k.party.CreateOrganizationRequest;
import io.tchepannou.k.party.CreateOrganizationResponse;
import io.tchepannou.k.party.GetOrganizationResponse;
import io.tchepannou.k.party.UpdateOrganizationRequest;
import io.tchepannou.k.party.UpdateOrganizationResponse;
import io.tchepannou.k.party.dao.OrganizationDao;
import io.tchepannou.k.party.domain.Organization;
import io.tchepannou.k.party.domain.Party;
import io.tchepannou.k.party.domain.PartyType;
import io.tchepannou.k.party.exception.Error;
import io.tchepannou.k.party.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService extends BasePartyService {
    @Autowired
    private OrganizationDao organizationDao;

    @Transactional
    public CreateOrganizationResponse create(CreateOrganizationRequest request) {
        final Party party = createParty(PartyType.ID_ORGANIZATION);

        final Organization org = new Organization();
        org.setId(party.getId());
        org.setName(request.getName());
        organizationDao.save(org);

        final CreateOrganizationResponse response = new CreateOrganizationResponse();
        response.setId(org.getId());
        return response;
    }

    @Transactional
    public UpdateOrganizationResponse update(Integer id, UpdateOrganizationRequest request) {
        final Organization org = organizationDao.findOne(id);
        if (org == null){
            throw new NotFoundException(Error.PARTY_NOT_FOUND);
        }

        org.setName(request.getName());
        organizationDao.save(org);

        final UpdateOrganizationResponse response = new UpdateOrganizationResponse();
        response.setId(org.getId());
        return response;
    }

    public GetOrganizationResponse findById(Integer id){
        final Organization org = organizationDao.findOne(id);
        if (org == null){
            throw new NotFoundException(Error.PARTY_NOT_FOUND);
        }

        final PartyType partyType = partyTypeDao.findOne(PartyType.ID_ORGANIZATION);
        final io.tchepannou.k.party.PartyType type = new io.tchepannou.k.party.PartyType();
        type.setId(partyType.getId());
        type.setName(partyType.getName());

        final GetOrganizationResponse response = new GetOrganizationResponse();
        response.setId(org.getId());
        response.setName(org.getName());
        response.setPartyType(type);

        return response;
    }
}
