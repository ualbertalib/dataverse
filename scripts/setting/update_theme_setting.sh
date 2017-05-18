#!/bin/sh
psql -U postgres dvndb < setting.sql
psql -U postgres dvndb < dataversethemes.sql
tar -xf logos.tar.gz -C /usr/local/glassfish4/glassfish/domains/domain1/docroot