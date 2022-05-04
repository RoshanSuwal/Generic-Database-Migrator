package org.ekbana.cassandraMigrator

import org.apache.spark.sql.SparkSession
import org.ekbana.cassandraMigrator.model.SparkConfiguration

class SparkConnection {

  def init(sparkConfiguration: SparkConfiguration):SparkSession= {

    val spark: SparkSession = SparkSession
      .builder()
//      .config("org.ekbana.bigdata.spark.debug.maxToStringFields", 1000)
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .appName(sparkConfiguration.getAppName)
      .master(sparkConfiguration.getMaster)
      .getOrCreate()
    spark
  }
}

//object SparkConnection{
//  val getSparkContext=new SparkConnection().init();
//}
