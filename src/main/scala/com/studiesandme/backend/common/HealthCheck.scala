package com.studiesandme.backend.common

import scala.concurrent.Future

sealed trait Health
case object Healthy extends Health
final case class Unhealthy(service: String, reason: String) extends Health {
  override def toString: String = s"$service unavailabe. Reason: $reason."
}
final case class Unknown(service: Option[String], reason: String) extends Health {
  override def toString: String = service match {
    case Some(serviceName) => s"$serviceName status unknown. Reason: $reason"
    case None              => s"Status check failed for unknown service. Reason: $reason"
  }
}

trait HealthCheck {
  def isHealthy: Future[Health]
}
