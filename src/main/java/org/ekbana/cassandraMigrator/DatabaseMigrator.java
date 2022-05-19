package org.ekbana.cassandraMigrator;

import org.apache.log4j.BasicConfigurator;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.storage.StorageLevel;
import org.ekbana.cassandraMigrator.model.MigratorConfiguration;
import org.ekbana.cassandraMigrator.transformer.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DatabaseMigrator {

    public static final Logger logger = LoggerFactory.getLogger(DatabaseMigrator.class);

    public static void run(String[] args) throws FileNotFoundException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String config = System.getProperty("config");
//        BasicConfigurator.configure();
        //String config = "/home/samir/Desktop/ekbana/DatabaseMigrator1/databasemigrator/conf.yml";

        logger.info("Configuration file from {}", config);
        InputStream inputStream = new FileInputStream(new File(config));
        Yaml yaml = new Yaml(new Constructor(MigratorConfiguration.class));
        MigratorConfiguration migratorConfiguration = yaml.load(inputStream);

        SparkSession sparkSession = new SparkConnection().init(migratorConfiguration.getSparkConfiguration());

        GenericMigrator genericMigrator = new GenericMigrator();

        logger.info("[Transformer] : loading {} ", migratorConfiguration.getTransformer());
        final Class<?> aClass = Class.forName(migratorConfiguration.getTransformer().trim());
        final Transformer transformer = (Transformer) aClass.newInstance();

        Dataset<Row> rowDataset = genericMigrator.loadFrom(sparkSession, migratorConfiguration.getSourceProps());
        rowDataset.printSchema();

//        rowDataset.explain(true);

        rowDataset=rowDataset.filter("created_at < '2021-01-01 00:00:00'");
        rowDataset.show();

        rowDataset.explain(true);

//        rowDataset=rowDataset.repartition(1000);

//        logger.info("Total records count : {} ",rowDataset.count());

//        logger.info("count : {}",rowDataset.count());
//        rowDataset = transformer.transform(sparkSession, rowDataset);
//        genericMigrator.loadTo(rowDataset, migratorConfiguration.getSinkProps());
//        genericMigrator.loadAfterChunking(rowDataset,migratorConfiguration.getSinkProps(),sparkSession);

//        while (true);s
    }
}
