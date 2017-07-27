package io.tchepannou.k.party.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.tchepannou.k.party.CreateOrganizationRequest;
import io.tchepannou.k.party.CreateOrganizationResponse;
import io.tchepannou.k.party.ErrorResponse;
import io.tchepannou.k.party.GetOrganizationResponse;
import io.tchepannou.k.party.UpdateOrganizationRequest;
import io.tchepannou.k.party.UpdateOrganizationResponse;
import io.tchepannou.k.party.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/party/v1")
@Api(value = "/party/v1", description = "Organizations")
public class OrganizationController {

    @Autowired
    OrganizationService service;

    @RequestMapping(path = "/organizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create", notes = "Create a new organization")
    public CreateOrganizationResponse create(@RequestBody CreateOrganizationRequest request){
        return service.create(request);
    }

    @RequestMapping(path = "/organization/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update", notes = "Update a new organization")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success"),
            @ApiResponse(code=404, message = "Organization not found", response = ErrorResponse.class)
    })
    public UpdateOrganizationResponse update(@PathVariable Integer id, @RequestBody UpdateOrganizationRequest request){
        return service.update(id, request);
    }

    @RequestMapping(path = "/organization/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findById", notes = "Find an organization by ID")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success"),
            @ApiResponse(code=404, message = "Organization not found", response = ErrorResponse.class)
    })
    public GetOrganizationResponse findById (@PathVariable Integer id){
        return service.findById(id);
    }
}
