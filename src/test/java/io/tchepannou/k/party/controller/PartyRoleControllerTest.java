package io.tchepannou.k.party.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tchepannou.k.party.GrantPartyRoleRequest;
import io.tchepannou.k.party.dao.PartyRoleDao;
import io.tchepannou.k.party.dao.PartyRoleTypeDao;
import io.tchepannou.k.party.domain.PartyRole;
import io.tchepannou.k.party.domain.PartyRoleType;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql({"/sql/clean.sql", "/sql/PartyRoleController.sql"})
public class PartyRoleControllerTest {
    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PartyRoleDao partyRoleDao;

    @Autowired
    private PartyRoleTypeDao partyRoleTypeDao;


    @Before
    public void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void shouldReturnPartyRoles() throws Exception {
        mockMvc
                .perform(get("/party/v1/roles/party/200"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyRoles.length()", is(2)))
                .andExpect(jsonPath("$.partyRoles[0].id", is(200)))
                .andExpect(jsonPath("$.partyRoles[0].partyRoleType.id", is(200)))
                .andExpect(jsonPath("$.partyRoles[0].partyRoleType.name", is("BUS_OPERATOR")))
                .andExpect(jsonPath("$.partyRoles[1].id", is(201)))
                .andExpect(jsonPath("$.partyRoles[1].partyRoleType.id", is(201)))
                .andExpect(jsonPath("$.partyRoles[1].partyRoleType.name", is("AIR_OPERATOR")))
        ;
    }

    @Test
    public void shouldReturnEmptyForPartyRolesWithInvalidPartyId() throws Exception {
        mockMvc
                .perform(get("/party/v1/roles/party/99999"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyRoles.length()", is(0)))
        ;
    }

    @Test
    public void shouldReturnPartyRole() throws Exception {
        mockMvc
                .perform(get("/party/v1/role/BUS_OPERATOR/party/200"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyRole.id", is(200)))
                .andExpect(jsonPath("$.partyRole.partyRoleType.id", is(200)))
                .andExpect(jsonPath("$.partyRole.partyRoleType.name", is("BUS_OPERATOR")))
        ;
    }

    @Test
    public void shouldReturn404ForPartyRoleWithInvalidPartyId() throws Exception {
        mockMvc
                .perform(get("/party/v1/role/BUS_OPERATOR/party/99999"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldReturn404ForPartyRoleWithInvalidRole() throws Exception {
        mockMvc
                .perform(get("/party/v1/role/xxxxx/party/200"))

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldGrantRole() throws Exception {
        // Given
        final GrantPartyRoleRequest req = new GrantPartyRoleRequest();
        req.setRoleName("BUS_OPERATOR");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/roles/party/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                ;

        // Then
        final List<PartyRole> roles = partyRoleDao.findByPartyId(100);
        assertThat(roles).hasSize(1);

        final PartyRoleType partyRoleType = partyRoleTypeDao.findOne(roles.get(0).getPartyRoleTypeId());
        assertThat(partyRoleType.getName()).isEqualTo("BUS_OPERATOR");
    }

    @Test
    public void shouldGrantRoleTwice() throws Exception {
        // Given
        final GrantPartyRoleRequest req = new GrantPartyRoleRequest();
        req.setRoleName("BUS_OPERATOR");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/roles/party/101")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        ;

        // Then
        final List<PartyRole> roles = partyRoleDao.findByPartyId(101);
        assertThat(roles).hasSize(1);
    }

    @Test
    public void shouldReturn404WhenGrantingRoleToInvalidParty() throws Exception {
        // Given
        final GrantPartyRoleRequest req = new GrantPartyRoleRequest();
        req.setRoleName("BUS_OPERATOR");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/roles/party/9999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void shouldReturn400WhenGrantingInvalidRole() throws Exception {
        // Given
        final GrantPartyRoleRequest req = new GrantPartyRoleRequest();
        req.setRoleName("XXXX");

        // When
        final String jsonRequest = mapper.writeValueAsString(req);
        mockMvc
                .perform(
                        post("/party/v1/roles/party/101")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                )

                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
        ;
    }
}
