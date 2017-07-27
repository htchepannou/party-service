package io.tchepannou.k.party.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.tchepannou.k.party.CreatePersonRequest;
import io.tchepannou.k.party.CreatePersonResponse;
import io.tchepannou.k.party.ErrorResponse;
import io.tchepannou.k.party.GetPersonResponse;
import io.tchepannou.k.party.UpdatePersonRequest;
import io.tchepannou.k.party.UpdatePersonResponse;
import io.tchepannou.k.party.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/party/v1")
@Api(value = "/party/v1", description = "People")
public class PersonController {

    @Autowired
    PersonService service;

    @RequestMapping(path = "/people", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "create", notes = "Create a new person")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success")
    })
    public CreatePersonResponse create(@RequestBody CreatePersonRequest request){
        return service.create(request);
    }

    @RequestMapping(path = "/person/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "update", notes = "Update a new person")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success"),
            @ApiResponse(code=404, message = "Person not found", response = ErrorResponse.class)
    })
    public UpdatePersonResponse update(@PathVariable Integer id, @RequestBody UpdatePersonRequest request){
        return service.update(id, request);
    }

    @RequestMapping(path = "/person/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "findById", notes = "Find an person by ID")
    @ApiResponses({
            @ApiResponse(code=200, message = "Success"),
            @ApiResponse(code=404, message = "Person not found", response = ErrorResponse.class)
    })
    public GetPersonResponse findById (@PathVariable Integer id){
        return service.findById(id);
    }
}
