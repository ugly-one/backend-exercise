package com.studiesandme.backend.common

import javax.inject.{Inject, Singleton}
import org.flywaydb.core.Flyway

trait MigrationService {
  def migrate():        Unit
  def getSchemaVersion: Seq[String]
}

@Singleton
class MigrationServiceImpl @Inject() (configValues: ConfigValues) extends MigrationService {
  val clusterIds: Seq[ClusterId] = Clusters.active

  val flyway: Seq[Flyway] = {
    clusterIds.map { clusterId =>
      val c = configValues.Db.connectionConfig(clusterId)

      val f = new Flyway
      f.setDataSource(c.url, c.user, c.password)
      f
    }
  }

  if (configValues.Db.migrateOnStartup) {
    retry(configValues.Db.connectionTimeout)(migrate())
  }

  def migrate() = {
    flyway.foreach { f =>
      f.repair()
      f.migrate()
    }
  }

  def getSchemaVersion: Seq[String] = {
    flyway.map(_.getBaselineVersion.toString)
  }

  @annotation.tailrec
  final def retry[T](n: Int)(fn: => T): T = {
    util.Try { fn } match {
      case util.Success(x) => x
      case _ if n > 1 => {
        Thread.sleep(1000)
        retry(n - 1)(fn)
      }
      case util.Failure(e) => throw e
    }
  }

}
