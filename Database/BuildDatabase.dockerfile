FROM mysql:8.0.22

COPY ./initial.sql /docker-entrypoint-initdb.d/initial.sql
COPY ./test-data.sql /docker-entrypoint-initdb.d/test-data.sql
COPY ./production-data.sql /docker-entrypoint-initdb.d/production-data.sql
