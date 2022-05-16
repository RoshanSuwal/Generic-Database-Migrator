package org.ekbana.cassandraMigrator.transformer;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public abstract class Transformer {
    public abstract Dataset<Row> transform(SparkSession sparkSession,Dataset<Row> dataset);
}
