package com.elsevier

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, SparkSession}

case class Movie(movieId: Int, title: String)
case class Rating(userId: Int, movieId: Int, rating: Int)

class RecommendMovies(val userId: Int = 0) {

  def run(): Unit = {

    implicit val spark = SparkSession.builder
      .master("local")
      .appName("Recommend Movies")
      .getOrCreate()

    import spark.implicits._
    val moviedDs = getMovies
    val ratingDs = getRatings
    //moviedDs.show(false)
    ratingDs.show(false)

    val userRatingDs = ratingDs.where($"userId" === userId)
    userRatingDs.show(false)
  }

  def getMovies(implicit spark: SparkSession): Dataset[Movie] = {
    implicit val encoder = Encoders.product[Movie]
    getDF("src/main/resources/ml-100k/u.item", encoder.schema, "|").as[Movie]
  }

  def getRatings(implicit spark: SparkSession): Dataset[Rating] = {
    implicit val encoder = Encoders.product[Rating]
    getDF("src/main/resources/ml-100k/u.data", encoder.schema, "\\t").as[Rating]
  }

  def getDF(fromFile: String, schema: StructType, delimiter: String)(implicit spark: SparkSession): DataFrame = {
    spark.read
      .option("delimiter", delimiter)
      .option("header", "false")
      .schema(schema)
      .csv(fromFile)
  }

}

object RecommendMovies {

  def main(args: Array[String]): Unit = {
    new RecommendMovies().run()
  }
}
