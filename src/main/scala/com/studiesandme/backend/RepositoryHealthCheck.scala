package com.studiesandme.backend

import com.studiesandme.backend.common.{ClientId, DBEnv, Health, HealthCheck, Healthy, Unhealthy}

import scala.concurrent.{ExecutionContext, Future}

trait RepositoryHealthCheck extends HealthCheck {
  implicit val ec: ExecutionContext

  def isHealthy(dbEnv: DBEnv, repoName: String): Future[Health] = {
    import dbEnv.driver.api._
    dbEnv.db(ClientId("platform")).run { sql"SELECT 1 + 1".as[Int].head }
  }.map {
      case 2 => Healthy
      case x =>
        Unhealthy(
          s"$repoName",
          s"Cannot perform simple SELECT, result was $x and expected was 2",
        )
    }
    .recover {
      case e: Throwable => Unhealthy(s"$repoName", e.getMessage)
    }

}
