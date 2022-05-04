package org.ekbana.cassandraMigrator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.ekbana.cassandraMigrator.model.MigratorConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

public class Launcher {
    public static void main(String[] args) throws FileNotFoundException {

        String config = System.getProperty("config");
        System.out.println("Loading configuration file from " + config);
        InputStream inputStream = new FileInputStream(new File(config));
        Yaml yaml = new Yaml(new Constructor(MigratorConfiguration.class));
        MigratorConfiguration migratorConfiguration = yaml.load(inputStream);

//        CassandraProps source= CassandraProps.builder()
//                .host("127.0.0.1")
//                .keyspace("bigmart")
//                .table("collection")
//                .build();
//
//        CassandraProps destCassandraModel= CassandraProps.builder()
//                .host("localhost")
//                .keyspace("bigmart")
//                .table("collection1")
//                .build();

        String[] sourceTables = migratorConfiguration.getCassandraSource().getTables().split(",");
        String[] destTables = migratorConfiguration.getCassandraDestination().getTables().split(",");

        if (sourceTables.length == destTables.length) {
            SparkSession sparkSession = new SparkConnection().init(migratorConfiguration.getSparkConfiguration());
            CassandraTableMigrator cassandraTableMigrator = new CassandraTableMigrator();
            for (int i = 0; i < sourceTables.length; i++) {
                System.out.println("source :" + sourceTables[i] + "\tdest :" + destTables[i]);
//                Long[] counts = new Long[10];
//                for (int j = 0; j < 10; j++) {
                Dataset<Row> rowDataset = cassandraTableMigrator.loadFrom(sparkSession, migratorConfiguration.getCassandraSource(), sourceTables[i]);
                rowDataset.printSchema();
//                    counts[j] = rowDataset.count();
//                System.out.println(rowDataset.count());
//                }
//                Arrays.stream(counts).forEach(c -> System.out.println(c));
                cassandraTableMigrator.loadTo(migratorConfiguration.getCassandraDestination(), rowDataset, destTables[i]);
            }
        } else {
            System.out.println("table name mismatch");
            System.out.println(migratorConfiguration.getCassandraSource().getTables());
            System.out.println(migratorConfiguration.getCassandraDestination().getTables());
        }
    }
}
