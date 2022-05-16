#CASSANDRA MIGRATOR

Migrates data between the cassandra tables using spark.

### how to run?
```
java8 -Dconfig=path_to_config.yml -jar CassandraMigrator-1.0-SNAPSHOT.jar
```

### Yaml File configuration
```
## configuration of spark
sparkConfiguration :
  appName : Cassandra Migrator
  master : local[*]

## configuration of cassandra source
cassandraSource:
  host : source host address
  port : source host port
  username : user
  password : password
  keyStorePath : /path
  keyStorePassword : key-store-password
  trustStorePath : /path
  trustStorePassword : trust-store-password
  keyspace : key space
  tables : tablenames separated by comma(,)
  enableSSL : false # true if ssl is enabled

## configuration of cassandra destination
cassandraDestination:
  host : destination host address
  port : destination port
  username : user
  password : password
  keyStorePath : /path
  keyStorePassword : key-store-password
  trustStorePath : /path
  trustStorePassword : trust-store-password
  keyspace : keyspace name
  tables : tablenames separated by comma(,)
  enableSSL : false  # true if ssl is enabled
 
```
### Configuration for Version 3
```
sparkConfiguration:
  appName: spark app name
  master: local[*]

sourceProps:
  formats:
    - value: org.apache.spark.sql.cassandra

  options:
    - key: spark.cassandra.connection.host
      value: 10.10.5.20
    - key: spark.cassandra.connection.port
      value: 8125
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
