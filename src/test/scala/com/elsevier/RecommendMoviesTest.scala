package com.elsevier

import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfter


class RecommendMoviesTest extends UnitSpec with BeforeAndAfter {

  var recommender: RecommendMovies = _
  implicit lazy val spark: SparkSession = sparkSession
  val userId = 0

  before {
    recommender = new RecommendMovies(userId)
  }
  after { // after each test
  }

  "Recommender" should "load movies correctly" in {

    val moviedDs = recommender.getMovies
    assertResult(1682, "- incorrect movies count") {
      moviedDs.count()
    }
  }

  it should "load ratings correctly" in {

    val ratingDs = recommender.getRatings
    assertResult(100003, "- incorrect ratings count") {
      ratingDs.count()
    }
  }

  it should "load user ratings should contain 3 ratings" in {

    import spark.implicits._
    val userRatings = recommender.getRatings.where($"userId" === userId)
    val ratingList = userRatings.map(_.rating).collect

    ratingList should contain theSameElementsAs List(5, 5, 1)
  }

  ignore should "customer is >= 50 years, with correct name" in {
    fail("we need to fail or cancel test for some reason")
    assume(recommender.userId > 0, "- Using Test User")
  }


}
