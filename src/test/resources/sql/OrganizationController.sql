-- shouldUpdateOrganization
INSERT INTO T_PARTY(id, party_type_fk) VALUES(100, 2);
INSERT INTO T_ORGANIZATION(id, name) VALUES(100, 'foo');


-- shouldReturnOrganization
INSERT INTO T_PARTY(id, party_type_fk) VALUES(200, 2);
INSERT INTO T_ORGANIZATION(id, name) VALUES(200, 'shouldReturnOrganization');
