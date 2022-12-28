package org.ekbana.cassandraMigrator

import org.apache.spark.sql.functions.{col, from_json}
import org.apache.spark.sql.types.{MapType, StringType}
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import org.ekbana.cassandraMigrator.proximity.GeoUtils
import org.ekbana.cassandraMigrator.proximity.GeoUtils.LatLng

class GeoGridTransformer extends Serializable {

  def geoEncode(latitude: Double, longitude: Double, level: Int): String = {

    var str = "{";
    for (i <- 5 to level) {
      if (i > 5) {
        str = str + ","
      }
      val hash=GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), i)
      str = str + "\"level-"+i+"\":\""+hash+"\""
    }

    str = str + "}"

    return str
//    s"""{"5":"${GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), 5)}"
//      ,"10":"${GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), 10)}"
//      ,"15":"${GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), 15)}"
//      ,"20":"${GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), 20)}"
//      ,"25":"${GeoUtils.encodeGeoHash(new LatLng(latitude, longitude), 25)}"
//      }"""
  }

  def geoLocation(latitude: Double, longitude: Double): String = {
    s"""{"lat":"${latitude}"
      ,"lon":"${longitude}"
      }"""
  }

  def transform(sparkSession: SparkSession, dataframe: Dataset[Row]): Dataset[Row] = {
    var df = dataframe

    sparkSession.udf.register("geoEncode", (lat: Double, lng: Double, level: Int) => geoEncode(lat, lng, level))
    sparkSession.udf.register("geoLocation", (lat: Double, lng: Double) => geoLocation(lat, lng))
    df.createTempView("geoTable")


    return sparkSession.sql("select " +
      "address" +
      ",business_id" +
      ",categories" +
      ",city" +
      ",name" +
      ",postal_code" +
      ",state" +
      ",stars" +
      ",geoEncode(latitude,longitude,23) as geo_grid_str " +
      ",geoLocation(latitude,longitude) as location_str " +
      "from geoTable")
      .withColumn("geo_hash", from_json(col("geo_grid_str"), MapType(StringType, StringType)))
      .withColumn("location", from_json(col("location_str"), MapType(StringType, StringType)))
      .drop("geo_grid_str")
      .drop("location_str")
  }

}
