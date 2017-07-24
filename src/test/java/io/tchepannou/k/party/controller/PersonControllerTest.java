package io.tchepannou.k.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tchepannou.k.party.CreatePersonRequest;
import io.tchepannou.k.party.CreatePersonResponse;
import io.tchepannou.k.party.UpdatePersonRequest;
import io.tchepannou.k.party.dao.PartyDao;
import io.tchepannou.k.party.dao.PersonDao;
import io.tchepannou.k.party.domain.Party;
import io.tchepannou.k.party.domain.PartyType;
import io.tchepannou.k.party.domain.Person;
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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/sql/clean.sql", "/sql/PersonController.sql"})
public class PersonControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PartyDao partyDao;



    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void shouldCreatePerson() throws Exception {
        // Given
        final CreatePersonRequest req = new CreatePersonRequest();
        req.setFirstName("Herve");
        req.setLastName("Tchepannou");
        req.setBirthDate("1973-12-27");
        req.setGender("M");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        final String jsonResponse = mockMvc
                .perform(
                        post("/party/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                    .getResponse()
                    .getContentAsString()
        ;
        CreatePersonResponse resp = mapper.readValue(jsonResponse, CreatePersonResponse.class);

        // Then
        Person org = personDao.findOne(resp.getId());
        assertThat(org.getFirstName()).isEqualTo("Herve");
        assertThat(org.getLastName()).isEqualTo("Tchepannou");
        assertThat(new Date(org.getBirthDate().getTime())).isEqualTo("1973-12-27T00:00:00");
        assertThat(org.getGender()).isEqualTo(Person.GENDER_MALE);

        Party party = partyDao.findOne(org.getId());
        assertThat(party.getPartyTypeId()).isEqualTo(PartyType.ID_PERSON);
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        // Given
        final UpdatePersonRequest req = new UpdatePersonRequest();
        req.setFirstName("Herve");
        req.setLastName("Tchepannou");
        req.setBirthDate("1973-12-27");
        req.setGender("M");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        final String jsonResponse = mockMvc
                .perform(
                        post("/party/v1/person/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                ;
        CreatePersonResponse resp = mapper.readValue(jsonResponse, CreatePersonResponse.class);

        // Then
        final Person org = personDao.findOne(resp.getId());
        assertThat(org.getFirstName()).isEqualTo("Herve");
        assertThat(org.getLastName()).isEqualTo("Tchepannou");
        assertThat(new Date(org.getBirthDate().getTime())).isEqualTo("1973-12-27T00:00:00");
        assertThat(org.getGender()).isEqualTo(Person.GENDER_MALE);
    }

    @Test
    public void shouldReturn404WhenUpdatingPersonWithInvalidId() throws Exception {
        // Given
        final UpdatePersonRequest req = new UpdatePersonRequest();
        req.setFirstName("shouldUpdatePerson");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/person/99999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldReturnPerson() throws Exception {
        // When/Then
        mockMvc
                .perform(get("/party/v1/person/200"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Ray")))
                .andExpect(jsonPath("$.lastName", is("Sponsible")))
                .andExpect(jsonPath("$.gender", is("M")))
                .andExpect(jsonPath("$.birthDate", is("1975-04-08")))
                .andExpect(jsonPath("$.partyType.id", is(PartyType.ID_PERSON)))
                .andExpect(jsonPath("$.partyType.name", is("PERSON")))
        ;

    }
}
