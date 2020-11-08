package com.studiesandme.backend.common

import akka.http.scaladsl.testkit.RouteTestTimeout
import org.scalatest.mockito.MockitoSugar

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Extends StandardSpec with features to aid unit testing including:
  *   - Support for mockito mocking
  *   - Defines implicits required for future, akka and spray route testing
  */
trait UnitTestSupport extends MockitoSugar {
  suite: StandardSpec =>

  override protected implicit def routeTestTimeout: RouteTestTimeout = {
    RouteTestTimeout(2 seconds)
  }

  def randomString(length: Int = 8) = scala.util.Random.alphanumeric.take(length).mkString

}
