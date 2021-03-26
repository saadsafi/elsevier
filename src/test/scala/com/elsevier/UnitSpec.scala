package com.elsevier

import org.scalatest._
import flatspec._
import matchers._
import org.apache.spark.sql.SparkSession

abstract class UnitSpec extends AnyFlatSpec
  with should.Matchers
  with OptionValues
  with Inside
  with Inspectors {
  val sparkSession = SparkSession.builder
    .master("local")
    .appName("Recommend Movies")
    .getOrCreate()

}
