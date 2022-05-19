package org.ekbana.cassandraMigrator

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.ekbana.cassandraMigrator.model.Props
import org.slf4j.{Logger, LoggerFactory}

class GenericMigrator {

  val logger: Logger = LoggerFactory.getLogger(classOf[GenericMigrator])

  def loadFrom(sparkSession: SparkSession, sourceProps: Props): DataFrame = {
    var df = sparkSession.read
    logger.info("Configuring the source spark dataframe")
    logger.info("sparkSession.read")
    sourceProps.getFormats.forEach(x => {
      df = df.format(x.getValue.trim)
      logger.info("\t.format( {} )", x.getValue)
    })
    sourceProps.options.forEach(x => {
      df = df.option(x.getKey.trim, x.getValue.trim)
      logger.info(s"\t.option( ${x.getKey} , ${x.getValue} ) ")
    })

    logger.info("\t.load()")
    var dataFrame = df.load()

    sourceProps.filters.forEach(x=>{
      logger.info(s"\t.filter( ${x})")
      dataFrame=dataFrame.filter(x);
    })

    dataFrame
  }

  def loadAfterChunking(dataFrame: DataFrame, sinkProps: Props, sparkSession: SparkSession): Unit = {
    //    dataFrame.foreachPartition {
    //      partition=>logger.info("partition size : {} ",partition.size)
    //    }
  }

  def loadTo(dataFrame: DataFrame, sourceProps: Props): Unit = {

    var df = dataFrame.write
    logger.info("Configuring the sink spark dataframe")
    logger.info("dataframe.write")
    if (sourceProps.formats != null) {
      sourceProps.formats.forEach(x => {
        df = df.format(x.getValue)
        logger.info(s"\t.format( ${x.getValue} )")
      })
    }
    if (sourceProps.modes != null) {
      sourceProps.modes.forEach(x => {
        df = df.mode(x.getValue)
        logger.info(s"\t.mode( ${x.getValue})")
      })
    }
    if (sourceProps.options != null) {
      sourceProps.options.forEach(x => {
        df = df.option(x.getKey, x.getValue)
        //        logger.info("\t.option({},{}) ",x.getKey,x.getValue)
        logger.info(s"\t.option( ${x.getKey}, ${x.getValue}) ")
      })
    }
    logger.info("\t.save()")
    df.save()
  }
}
