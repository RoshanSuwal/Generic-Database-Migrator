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

###Note
- tables must be already create in the destination with same schema
- runs only in java-8
- table names must match in source and destination
- only the tables from one keyspace can be extracted and dumped too
