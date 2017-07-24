package io.tchepannou.k.party.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table( name = "T_PARTY_ROLE")
public class PartyRole extends Persistent{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="party_fk")
    private Integer partyId;

    @Column(name="party_role_type_fk")
    private Integer partyRoleTypeId;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(final Integer partyId) {
        this.partyId = partyId;
    }

    public Integer getPartyRoleTypeId() {
        return partyRoleTypeId;
    }

    public void setPartyRoleTypeId(final Integer partyRoleTypeId) {
        this.partyRoleTypeId = partyRoleTypeId;
    }
}
