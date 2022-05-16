package org.ekbana.cassandraMigrator.transformer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.ekbana.cassandraMigrator.DatabaseMigrator;

public class DefaultTransformer extends Transformer {

    @Override
    public Dataset<Row> transform(SparkSession sparkSession, Dataset<Row> dataset) {
        DatabaseMigrator.logger.info("[transformer] executing : "+DefaultTransformer.class.getSimpleName());
        return dataset;
    }
}
