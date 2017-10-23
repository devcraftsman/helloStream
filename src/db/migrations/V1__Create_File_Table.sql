CONNECT AS TEST;

CREATE TABLE TEST.PROCESSING_FILES
(
    FILE_ID  NUMBER GENERATED BY DEFAULT AS IDENTITY
    FILENAME VARCHAR2(4000 byte) NOT NULL,
    INSERT_DATE TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP NOT NULL,
    PROCCESSING_NODE VARCHAR(256 byte) DEFAULT "-"
);

CREATE UNIQUE INDEX TEST.PROCESSING_FILE_PK
    ON TEST.PROCESSING_FILES (FILE_ID);