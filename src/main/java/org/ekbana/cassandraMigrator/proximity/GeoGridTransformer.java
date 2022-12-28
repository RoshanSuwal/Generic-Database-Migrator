package org.ekbana.cassandraMigrator.proximity;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.ekbana.cassandraMigrator.transformer.Transformer;

public class GeoGridTransformer extends Transformer {
    @Override
    public Dataset<Row> transform(SparkSession sparkSession, Dataset<Row> dataset) {
        return new org.ekbana.cassandraMigrator.GeoGridTransformer().transform(sparkSession,dataset);
    }
}
