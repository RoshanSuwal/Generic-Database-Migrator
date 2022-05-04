package org.ekbana.cassandraMigrator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class MigratorConfiguration {
    // spark properties
    private SparkConfiguration sparkConfiguration;
    // cassandra source properties
    private CassandraProps cassandraSource;
    // cassandra destination properties
    private CassandraProps cassandraDestination;

}
