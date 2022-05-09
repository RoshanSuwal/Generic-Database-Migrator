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

        //String config = System.getProperty("config");
        String config = "/home/samir/Desktop/ekbana/DatabaseMigrator1/databasemigrator/conf.yml";

        System.out.println("Configuration file from " + config);
        InputStream inputStream = new FileInputStream(new File(config));
        Yaml yaml = new Yaml(new Constructor(MigratorConfiguration.class));
        MigratorConfiguration migratorConfiguration = yaml.load(inputStream);

        SparkSession sparkSession = new SparkConnection().init(migratorConfiguration.getSparkConfiguration());

        GenericMigrator genericMigrator = new GenericMigrator();

        Dataset<Row> rowDataset = genericMigrator.loadFrom(sparkSession, migratorConfiguration.getSourceProps());

        // opertions
        System.out.println("[SIZE] : " + rowDataset.count() + "\n");

        genericMigrator.loadTo(rowDataset, migratorConfiguration.getSinkProps());
    }
}
