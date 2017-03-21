CREATE TABLE PERSON (
  PERSON_ID             NUMBER                    NOT NULL,
  LOGIN                 VARCHAR2(120)             NOT NULL,
  FIRST_NAME            VARCHAR2(120)             NOT NULL,
  LAST_NAME             VARCHAR2(120)             NOT NULL,
  CONSTRAINT PERSON_PK PRIMARY KEY (PERSON_ID),
  UNIQUE (LOGIN)
);

CREATE TABLE ACCOUNT (
  ACCOUNT_ID            NUMBER                    NOT NULL,
  NUMBER                NUMBER                    NOT NULL,
  CURRENCY              VARCHAR2(120)             NOT NULL,
  BALANCE               NUMBER                    NOT NULL,
  PERSON_ID             VARCHAR2(120)             NOT NULL,
  CONSTRAINT ACCOUNT_PK PRIMARY KEY (ACCOUNT_ID),
  FOREIGN KEY (PERSON_ID)
    REFERENCES PERSON (PERSON_ID),
  UNIQUE (NUMBER)
);