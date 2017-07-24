--- schemas
CREATE TABLE T_PARTY_TYPE (
  id          INT          NOT NULL AUTO_INCREMENT,
  name        VARCHAR(64),
  description TEXT,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_PARTY_ROLE_TYPE (
  id          INT          NOT NULL AUTO_INCREMENT,
  name        VARCHAR(64),
  description TEXT,

  PRIMARY KEY (id)
) ENGINE = InnoDB;



CREATE TABLE T_PARTY (
  id            INT       NOT NULL AUTO_INCREMENT,

  party_type_fk INT       NOT NULL REFERENCES T_PARTY_TYPE(id),

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_PARTY_ROLE(
  id                    INT       NOT NULL AUTO_INCREMENT,

  party_fk              INT       NOT NULL REFERENCES T_PARTY(id),
  party_role_type_fk    INT       NOT NULL REFERENCES T_PARTY_ROLE_TYPE(id),

  insert_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP,
  update_timestamp  DATETIME   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE (party_fk, party_role_type_fk)

) ENGINE = InnoDB;



CREATE TABLE T_PERSON (
  id                INT           NOT NULL REFERENCES T_PARTY(id),

  first_name        VARCHAR(200),
  last_name         VARCHAR(200),
  gender            INT,
  birth_date        DATE,

  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE T_ORGANIZATION (
  id      INT  NOT NULL REFERENCES T_PARTY(id),

  name    VARCHAR(200),

  PRIMARY KEY (id)
) ENGINE = InnoDB;


--- Data
INSERT INTO T_PARTY_TYPE(id, name) VALUES
  (1, 'PERSON'),
  (2, 'ORGANIZATION')
;

INSERT INTO T_PARTY_ROLE_TYPE(id, name) VALUES
  (100, 'CUSTOMER'),

  (200, 'BUS_OPERATOR'),
  (201, 'AIR_OPERATOR'),
  (202, 'RAILS_OPERATOR'),
  (203, 'LODGING_OPERATOR'),

  (300, 'PAYMENT_GATEWAY')
;
