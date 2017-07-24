package io.tchepannou.k.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tchepannou.k.party.CreateOrganizationRequest;
import io.tchepannou.k.party.CreateOrganizationResponse;
import io.tchepannou.k.party.UpdateOrganizationRequest;
import io.tchepannou.k.party.dao.OrganizationDao;
import io.tchepannou.k.party.dao.PartyDao;
import io.tchepannou.k.party.domain.Organization;
import io.tchepannou.k.party.domain.Party;
import io.tchepannou.k.party.domain.PartyType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/sql/clean.sql", "/sql/OrganizationController.sql"})
public class OrganizationControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private PartyDao partyDao;



    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateOrganization() throws Exception {
        // Given
        final CreateOrganizationRequest req = new CreateOrganizationRequest();
        req.setName("shouldCreateOrganization");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        final String jsonResponse = mockMvc
                .perform(
                        post("/party/v1/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                    .getResponse()
                    .getContentAsString()
        ;
        CreateOrganizationResponse resp = mapper.readValue(jsonResponse, CreateOrganizationResponse.class);

        // Then
        Organization org = organizationDao.findOne(resp.getId());
        assertThat(org.getName()).isEqualTo("shouldCreateOrganization");

        Party party = partyDao.findOne(org.getId());
        assertThat(party.getPartyTypeId()).isEqualTo(PartyType.ID_ORGANIZATION);
    }

    @Test
    public void shouldUpdateOrganization() throws Exception {
        // Given
        final UpdateOrganizationRequest req = new UpdateOrganizationRequest();
        req.setName("shouldUpdateOrganization");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        final String jsonResponse = mockMvc
                .perform(
                        post("/party/v1/organization/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                ;
        CreateOrganizationResponse resp = mapper.readValue(jsonResponse, CreateOrganizationResponse.class);

        // Then
        final Organization org = organizationDao.findOne(resp.getId());
        assertThat(org.getName()).isEqualTo("shouldUpdateOrganization");
    }

    @Test
    public void shouldReturn404WhenUpdatingOrganizationWithInvalidId() throws Exception {
        // Given
        final UpdateOrganizationRequest req = new UpdateOrganizationRequest();
        req.setName("shouldUpdateOrganization");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/organization/99999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldReturnOrganization() throws Exception {
        // When/Then
        mockMvc
                .perform(get("/party/v1/organization/200"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("shouldReturnOrganization")))
                .andExpect(jsonPath("$.partyType.id", is(PartyType.ID_ORGANIZATION)))
                .andExpect(jsonPath("$.partyType.name", is("ORGANIZATION")))
        ;

    }
}
