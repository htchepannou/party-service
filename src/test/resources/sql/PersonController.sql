-- shouldUpdatePerson
INSERT INTO T_PARTY(id, party_type_fk) VALUES(100, 1);
INSERT INTO T_PERSON(id, first_name) VALUES(100, 'foo');


-- shouldReturnOrganization
INSERT INTO T_PARTY(id, party_type_fk) VALUES(200, 1);
INSERT INTO T_PERSON(id, first_name, last_name, gender, birth_date) VALUES(200, 'Ray', 'Sponsible', 1, '1975-04-08');
