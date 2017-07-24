package io.tchepannou.k.party.dao;

import io.tchepannou.k.party.domain.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationDao extends CrudRepository<Organization, Integer> {
}
