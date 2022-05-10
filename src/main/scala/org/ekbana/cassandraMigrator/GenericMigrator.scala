package org.ekbana.cassandraMigrator

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.ekbana.cassandraMigrator.model.{Props}

class GenericMigrator {

  def loadFrom(sparkSession: SparkSession,sourceProps: Props): DataFrame = {
    var df = sparkSession.read
    sourceProps.getFormats.forEach(x=>{
      df = df.format(x.getValue.trim)
      println("[FORMAT] " + x.getValue)
    })
    sourceProps.options.forEach(x=>{
      df = df.option(x.getKey.trim, x.getValue.trim)
      println("[OPTION] " + x.getKey + " => " + x.getValue)
    })

    df.load()
  }

  def loadTo(dataFrame: DataFrame,sourceProps: Props): Unit = {
    var df = dataFrame.write
    if (sourceProps.formats != null) {
      sourceProps.formats.forEach(x => {
        df = df.format(x.getValue)
        println("[FORMAT] " + x.getValue)
      })
    }
    if (sourceProps.modes != null) {
      sourceProps.modes.forEach(x => {
        df = df.mode(x.getValue)
        println("[MODE] " + x.getValue)
      })
    }
    if (sourceProps.options != null) {
      sourceProps.options.forEach(x => {
        df = df.option(x.getKey, x.getValue)
        println("[OPTION] " + x.getKey + " => " + x.getValue)
      })
    }
    df.save()
  }
}
