package io.tchepannou.k.party.domain;

import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table( name = "T_ORGANIZATION")
public class Organization extends Persistent {
    @Id
    private Integer id;

    private String name;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
