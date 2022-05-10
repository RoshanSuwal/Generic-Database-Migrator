package org.ekbana.cassandraMigrator

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.ekbana.cassandraMigrator.model.CassandraProps

class CassandraTableMigrator {

  def loadFrom(sparkSession: SparkSession,cassandraProps: CassandraProps,table:String): DataFrame = {
    println("Loading from : "+cassandraProps)
    println("From Table : "+table)
    var dfReader = sparkSession.read

    if (cassandraProps.isEnableSSL) {
          sparkSession.read
            .format("org.apache.spark.sql.cassandra")
            .option("table", table)
            .option("keyspace", cassandraProps.getKeyspace)
            .option("spark.cassandra.connection.host", cassandraProps.getHost)
            .option("spark.cassandra.connection.port", cassandraProps.getPort)
            .option("spark.cassandra.auth.username", cassandraProps.getUsername)
            .option("spark.cassandra.auth.password", cassandraProps.getPassword)
            .option("spark.cassandra.connection.ssl.clientAuth.enabled", true)
            .option("spark.cassandra.connection.ssl.enabled", true)
            .option("spark.cassandra.connection.ssl.keyStore.path", cassandraProps.getKeyStorePath)
            .option("spark.cassandra.connection.ssl.keyStore.password", cassandraProps.getKeyStorePassword)
            .option("spark.cassandra.connection.ssl.trustStore.path", cassandraProps.getTrustStorePath)
            .option("spark.cassandra.connection.ssl.trustStore.password", cassandraProps.getTrustStorePassword)
            .option("spark.cassandra.input.consistency.level","ALL")
            .option("spark.cassandra.output.consistency.level","ALL")
            .option("spark.cassandra.read.timeoutMS","240000")
            //            .option("spark.cassandra.input.consistency.level","LOCAL_QUORUM")
            //            .option("spark.cassandra.output.consistency.level","LOCAL_QUORUM")
            .load()
    }else {
      sparkSession.read
        .format("org.apache.spark.sql.cassandra")
        .options(Map("table" -> table, "keyspace" -> cassandraProps.getKeyspace))
        .option("spark.cassandra.connection.host", cassandraProps.getHost)
        .option("spark.cassandra.connection.port", cassandraProps.getPort)
        .option("spark.cassandra.auth.username", cassandraProps.getUsername)
        .option("spark.cassandra.auth.password", cassandraProps.getPassword)
        //      .option("spark.sql.dse.search.enableOptimization","on")
        //      .option("spark.cassandra.input.fetch.size_in_rows", 10)
        .load()
    }

  }

  def loadTo(cassandraProps: CassandraProps, dataFrame: DataFrame,table:String): Unit = {
    println("Loading to : "+cassandraProps)
    println("To table :"+table)
    if(cassandraProps.isEnableSSL) {
          dataFrame.write
            .format("org.apache.spark.sql.cassandra")
            .mode("append")
            .option("table", table)
            .option("keyspace", cassandraProps.getKeyspace)
            .option("spark.cassandra.connection.host", cassandraProps.getHost)
            .option("spark.cassandra.connection.port", cassandraProps.getPort)
            .option("spark.cassandra.auth.username", cassandraProps.getUsername)
            .option("spark.cassandra.auth.password", cassandraProps.getPassword)
            .option("spark.cassandra.connection.ssl.clientAuth.enabled", true)
            .option("spark.cassandra.connection.ssl.enabled", true)
            .option("spark.cassandra.connection.ssl.keyStore.path", cassandraProps.getKeyStorePath)
            .option("spark.cassandra.connection.ssl.keyStore.password", cassandraProps.getKeyStorePassword)
            .option("spark.cassandra.connection.ssl.trustStore.path", cassandraProps.getTrustStorePath)
            .option("spark.cassandra.connection.ssl.trustStore.password", cassandraProps.getTrustStorePassword)
//            .option("spark.cassandra.input.consistency.level","ALL")
            .save()
    }else {

      dataFrame.write
        .format("org.apache.spark.sql.cassandra")
        .mode("append")
        .option("table", table)
        .option("keyspace", cassandraProps.getKeyspace())
        .option("spark.cassandra.connection.host", cassandraProps.getHost())
        .option("spark.cassandra.connection.port", cassandraProps.getPort)
        .option("spark.cassandra.auth.username", cassandraProps.getUsername)
        .option("spark.cassandra.auth.password", cassandraProps.getPassword)
        .save()
    }

  }
}
