-- shouldGrantRole
INSERT INTO T_PARTY(id, party_type_fk) VALUES(100, 2);

-- shouldGrantRoleTwice
INSERT INTO T_PARTY(id, party_type_fk) VALUES(101, 2);
INSERT INTO T_PARTY_ROLE(id, party_fk, party_role_type_fk) VALUE (101, 101, 200);

-- shouldReturnPartyRoles
INSERT INTO T_PARTY(id,party_type_fk) VALUES(200, 2);
INSERT INTO T_PARTY_ROLE(id, party_fk, party_role_type_fk) VALUE (200, 200, 200);
INSERT INTO T_PARTY_ROLE(id, party_fk, party_role_type_fk) VALUE (201, 200, 201);
