package io.tchepannou.k.party.service;

import io.tchepannou.k.party.GetPartyRoleResponse;
import io.tchepannou.k.party.GetPartyRolesResponse;
import io.tchepannou.k.party.GrantPartyRoleRequest;
import io.tchepannou.k.party.dao.PartyDao;
import io.tchepannou.k.party.dao.PartyRoleDao;
import io.tchepannou.k.party.dao.PartyRoleTypeDao;
import io.tchepannou.k.party.domain.Party;
import io.tchepannou.k.party.domain.PartyRole;
import io.tchepannou.k.party.domain.PartyRoleType;
import io.tchepannou.k.party.exception.Error;
import io.tchepannou.k.party.exception.InvalidRequestException;
import io.tchepannou.k.party.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PartyRoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PartyRoleService.class);

    @Autowired
    PartyDao partyDao;

    @Autowired
    PartyRoleDao partyRoleDao;

    @Autowired
    PartyRoleTypeDao partyRoleTypeDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    public void grant(Integer partyId, GrantPartyRoleRequest request){
        Party party = partyDao.findOne(partyId);
        if (party == null){
            throw new NotFoundException(Error.PARTY_NOT_FOUND);
        }

        final String roleName = request.getRoleName();
        final PartyRoleType roleType = partyRoleTypeDao.findByName(roleName);
        if (roleType == null){
            throw new InvalidRequestException(Error.INVALID_ROLE_NAME);
        }

        PartyRole  role = new PartyRole();
        role.setPartyId(party.getId());
        role.setPartyRoleTypeId(roleType.getId());

        final TransactionStatus tx = transactionManager.getTransaction(new DefaultTransactionAttribute());
        try {
            partyRoleDao.save(role);
            transactionManager.commit(tx);
        } catch (DataIntegrityViolationException e){
            LOGGER.warn("Party<{}> already have the role <{}>", partyId, roleName, e);
            transactionManager.rollback(tx);
        } catch (RuntimeException e){
            transactionManager.rollback(tx);
            throw e;
        }
    }

    public GetPartyRolesResponse findRoles(Integer partyId){
        final List<PartyRole> partyRoles = partyRoleDao.findByPartyId(partyId);

        final GetPartyRolesResponse response = new GetPartyRolesResponse();
        response.setPartyRoles(
                partyRoles.stream()
                .map(r -> toPartyRoleDto(r))
                .collect(Collectors.toList())
        );
        return response;
    }

    public GetPartyRoleResponse findRole(Integer partyId, String roleName){
        final PartyRoleType roleType  = partyRoleTypeDao.findByName(roleName);
        if (roleType == null){
            throw new NotFoundException(Error.INVALID_ROLE_NAME);
        }

        final PartyRole role = partyRoleDao.findByPartyIdAndPartyRoleTypeId(partyId, roleType.getId());
        if (role == null){
            throw new NotFoundException(Error.PARTY_ROLE_FOUND);
        }

        final GetPartyRoleResponse response = new GetPartyRoleResponse();
        response.setPartyRole(toPartyRoleDto(role));
        return response;
    }

    private io.tchepannou.k.party.PartyRole toPartyRoleDto (PartyRole role){
        final io.tchepannou.k.party.PartyRoleType partyRoleTypeDto = new io.tchepannou.k.party.PartyRoleType();
        PartyRoleType type = partyRoleTypeDao.findOne(role.getPartyRoleTypeId());
        partyRoleTypeDto.setId(type.getId());
        partyRoleTypeDto.setName(type.getName());

        final io.tchepannou.k.party.PartyRole partyRoleDto = new io.tchepannou.k.party.PartyRole();
        partyRoleDto.setId(role.getId());
        partyRoleDto.setPartyRoleType(partyRoleTypeDto);

        return partyRoleDto;
    }
}
