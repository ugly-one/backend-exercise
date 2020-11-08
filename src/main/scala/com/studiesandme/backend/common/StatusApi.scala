package com.studiesandme.backend.common

import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.{Directives, Route}
import com.google.inject.Inject
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future

class StatusApi @Inject() (healthChecks: Set[HealthCheck])
    extends RestComponent
    with SpecialExecutionTactics
    with StrictLogging
    with Directives {

  private def isUnhealthy(health: Health): Boolean = health match {
    case Healthy => false
    case _       => true
  }

  private def resolveHealthChecks(healthChecks: Set[HealthCheck]): Future[StatusCode] = {
    Future
      .sequence(healthChecks.map(_.isHealthy.recover { case e => Unknown(service = None, reason = e.getMessage) }))
      .map(_.filter(isUnhealthy))
      .map { unhealthyServices: Set[Health] =>
        unhealthyServices.size match {
          case 0 => StatusCodes.OK
          case x =>
            logger.info(s"$x checks did not report healthy")
            unhealthyServices.foreach {
              case x @ Unhealthy(_, _) => logger.info(x.toString)
              case x @ Unknown(_, _)   => logger.info(x.toString)
              case _                   => logger.error("Our code is borken")
            }
            StatusCodes.ServiceUnavailable
        }
      }
  }

  val route: Route = {
    path("v1" / "status") {
      get {
        complete(resolveHealthChecks(healthChecks))
      }
    }
  }
}
