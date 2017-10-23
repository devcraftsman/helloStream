create smallfile tablespace users datafile '/usr/lib/oracle/xe/oradata/XE/users.dbf' size 1g;

commit;

create user TEST_USER identified by TEST_USER DEFAULT TABLESPACE users QUOTA UNLIMITED ON users;

commit;

GRANT create session TO TEST_USER;
GRANT create table TO TEST_USER;
GRANT create view TO TEST_USER;
GRANT create any trigger TO TEST_USER;
GRANT create any procedure TO TEST_USER;
GRANT create sequence TO TEST_USER;
GRANT create synonym TO TEST_USER;

commit;