package com.studiesandme.backend.common

import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach, FlatSpec, Matchers}

import scala.concurrent.duration._

/**
  * Standard base class for tests.  Includes the following features:
  *
  *   - WordSpec style tests with Matcher DSL for assertions
  *
  *   - Support for testing Futures including the useful whenReady construct
  *
  *   - Support for testing spray Routes
  */
class StandardSpec
    extends FlatSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest
    with BeforeAndAfterEach
    with BeforeAndAfter {

  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds))

  protected implicit def routeTestTimeout = {
    RouteTestTimeout(1.seconds)
  }
}
