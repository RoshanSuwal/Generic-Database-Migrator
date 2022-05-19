package org.ekbana.cassandraMigrator

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.ekbana.cassandraMigrator.model.SparkConfiguration
import org.slf4j.{Logger, LoggerFactory}

class SparkConnection {

  val logger: Logger = LoggerFactory.getLogger(classOf[SparkConnection])

  def init(sparkConfiguration: SparkConfiguration):SparkSession= {

    val sparkConf=new SparkConf();
    logger.info("Configuring SparkSession")
    sparkConfiguration.configs.forEach( config => {
      sparkConf.set(config.getKey, config.getValue);
      logger.info(s"sparkConfig( ${config.getKey} , ${config.getValue})");
    })
    logger.info("\t.getOrCreate()")
    SparkSession.builder().config(sparkConf).getOrCreate()
//    val spark: SparkSession = SparkSession
//      .builder()
////      .config("org.ekbana.bigdata.spark.debug.maxToStringFields", 1000)
//      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
// //     .config("spark.jars", "/home/samir/Desktop/ekbana/DatabaseMigrator1/postgresql-42.3.5.jar")
//      .appName(sparkConfiguration.getAppName)
//      .master(sparkConfiguration.getMaster)
//      .config("spark.submit.deployMode","client")
//      .getOrCreate()
//    spark
  }
}

//object SparkConnection{
//  val getSparkContext=new SparkConnection().init();
//}
