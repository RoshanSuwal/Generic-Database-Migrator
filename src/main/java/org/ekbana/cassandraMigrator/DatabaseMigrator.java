package org.ekbana.cassandraMigrator;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
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
//        String config = System.getProperty("config");
        String config = args[0];
//        BasicConfigurator.configure();
        //String config = "/home/samir/Desktop/ekbana/DatabaseMigrator1/databasemigrator/conf.yml";

        logger.info("Configuration file from {}", config);
        InputStream inputStream = new FileInputStream(new File(config));
        Yaml yaml = new Yaml(new Constructor(MigratorConfiguration.class));
        MigratorConfiguration migratorConfiguration = yaml.load(inputStream);

//        MigratorConfiguration migratorConfiguration1=new MigratorConfiguration();

//        Props sourceProps=new Props();
//        sourceProps.setFormats(Arrays.asList(
//                new Props.Format("org.apache.spark.sql.cassandra")
//        ));
//
//        sourceProps.setOptions(Arrays.asList(
//                new Props.Option("spark.cassandra.connection.host","10.10.5.20"),
//                new Props.Option("spark.cassandra.connection.port","8125"),
//                new Props.Option("spark.cassandra.auth.username","cassandra"),
//                new Props.Option("spark.cassandra.auth.password","cassandra")
//
//        ));

        SparkSession sparkSession = new SparkConnection().init(migratorConfiguration.getSparkConfiguration());

        GenericMigrator genericMigrator = new GenericMigrator();

        logger.info("[Transformer] : loading {} ", migratorConfiguration.getTransformer());
        final Class<?> aClass = Class.forName(migratorConfiguration.getTransformer().trim());
        final Transformer transformer = (Transformer) aClass.newInstance();

        Dataset<Row> rowDataset = genericMigrator.loadFrom(sparkSession, migratorConfiguration.getSourceProps());
        rowDataset.printSchema();
        rowDataset.explain(true);

//        rowDataset=rowDataset.repartition(1000);

//        logger.info("Total records count : {} ",rowDataset.count());

//        logger.info("count : {}",rowDataset.count());
        rowDataset = transformer.transform(sparkSession, rowDataset);
        rowDataset.printSchema();
        rowDataset.show(false);
        genericMigrator.loadTo(rowDataset, migratorConfiguration.getSinkProps());
//        genericMigrator.loadAfterChunking(rowDataset,migratorConfiguration.getSinkProps(),sparkSession);

        sparkSession.cloneSession();
        sparkSession.close();
//        while (true);s
    }
}
