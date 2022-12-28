# Generic Database Migrator

A general solution to data migration using spark with data filtering, processing and transforming facilities.

### how to run?
```
java8 -Dconfig=path_to_config.yml -jar CassandraMigrator-1.0-SNAPSHOT.jar
```

### Yaml File configuration

- sample configuration file to migrate data from cassandra  table to postgresql table.

```
sparkConfiguration:
  appName: spark app name
  master: local[*]

sourceProps:
  formats:
    - value: org.apache.spark.sql.cassandra

  options:
    - key: spark.cassandra.connection.host
      value: localhost
    - key: spark.cassandra.connection.port
      value: 9042
    - key: spark.cassandra.auth.username
      value: cassandra
    - key: spark.cassandra.auth.password
      value: cassandra
    - key: spark.cassandra.connection.ssl.keyStore.path
      value: path-to-keystore 
    - key: spark.cassandra.connection.ssl.keyStore.password
      value: keystore password
    - key: spark.cassandra.connection.ssl.trustStore.path
      value: path to trust store
    - key: spark.cassandra.connection.ssl.trustStore.password
      value: truststore password
    - key: keyspace
      value: keyspace name
    - key: table
      value: table name
    - key: spark.cassandra.connection.ssl.clientAuth.enabled
      value: true or false
    - key: spark.cassandra.connection.ssl.enabled
      value: true
    - key: spark.cassandra.input.consistency.level
      value: ALL
    - key: spark.cassandra.output.consistency.level
      value: ALL
    - key: spark.cassandra.read.timeoutMS
      value: 240000

sinkProps:
  formats:
    - value: jdbc
  modes:
    - value : overwrite
  options:
    - key: driver
      value: org.postgresql.Driver
    - key: url
      value: jdbc:postgresql://localhost:54322/rad
    - key: dbtable
      value: database name
    - key: user
      value: auth user
    - key: password
      value: auth password

transformer : name of transformer
```
### Note
- tables must be already create in the destination with same schema
- runs only in java-8
- table names must match in source and destination
- only the tables from one keyspace can be extracted and dumped too
