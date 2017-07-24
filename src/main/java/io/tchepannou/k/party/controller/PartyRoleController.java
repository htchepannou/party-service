package io.tchepannou.k.party.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.tchepannou.k.party.GetPartyRoleResponse;
import io.tchepannou.k.party.GetPartyRolesResponse;
import io.tchepannou.k.party.GrantPartyRoleRequest;
import io.tchepannou.k.party.service.PartyRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/party/v1")
@Api(value = "/party/v1", description = "Party Roles")
public class PartyRoleController {

    @Autowired
    PartyRoleService service;

    @RequestMapping(path = "/roles/party/{partyId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "grantRole", notes = "Grant a Role to a Party")
    public void grant(
            @PathVariable Integer partyId,
            @RequestBody GrantPartyRoleRequest request
    ){
        service.grant(partyId, request);
    }

    @RequestMapping(path = "/roles/party/{partyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "getRoles", notes = "Return all the roles of a Party")
    public GetPartyRolesResponse getRoles(@PathVariable Integer partyId){
        return service.findRoles(partyId);
    }

    @RequestMapping(path = "/role/{roleName}/party/{partyId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "getRole", notes = "Return a role of a Party")
    public GetPartyRoleResponse getRole(@PathVariable String roleName, @PathVariable Integer partyId){
        return service.findRole(partyId, roleName);
    }
}
