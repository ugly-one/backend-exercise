package com.studiesandme.backend.common

import com.typesafe.config.{Config, ConfigFactory}

import scala.language.implicitConversions

/**
  * Created by martinjensen on 14/01/2017.
  *
  * Wrapper for accessing typesafe values (typically application.conf with fallback to reference.conf; see https://github.com/typesafehub/config)
  * We aggressively fail and don't make use of defaults in code, relying on a properly bundled configuration.
  * To make a value overridable using environment variables, use the typesafe pattern
  * group {
  *   value = "example"
  *   value = = \${?GROUP_VALUE}
  * }
  * and use default values that allow for applications to run in a developer setting.
  */
class ConfigValues {
  val config = ConfigFactory.load

  def dbConfigGroup: Config = config.getConfig("db")
  def dbConfigSubGroup(clusterId: ClusterId): Config = dbConfigGroup.getConfig(clusterId.toString.toLowerCase)

  object Db {
    import ConfigExt._
    def connectionConfig(clusterId: ClusterId): ConnectionConfig = {
      val url            = dbConfigSubGroup(clusterId).getString("url")
      val user           = dbConfigSubGroup(clusterId).getString("user")
      val pass           = dbConfigSubGroup(clusterId).getString("pass")
      val maxConnections = dbConfigSubGroup(clusterId).getIntOr("maxConnections", 20)
      ConnectionConfig(url, user, pass, maxConnections)
    }

    val migrateOnStartup:  Boolean = dbConfigGroup.getBoolean("migrateonstartup")
    val connectionTimeout: Int     = dbConfigGroup.getInt("connectiontimeout")
  }

  private def clientSecretsConfigGroup: Config = config.getConfig("clientSecrets")
  object ClientSecrets {
    def getClientSecret(clientId: ClientId) = {
      clientSecretsConfigGroup.getString(clientId.toString)
    }
  }

  object App {
    val name: String = config.getConfig("app").getString("name")
  }
}

class ConfigExt(val c: Config) extends AnyVal {
  def getIntOr(path: String, default: => Int = 0) = if (c.hasPath(path)) c.getInt(path) else default
}

object ConfigExt {
  @inline implicit def configExtDsl(c: Config): ConfigExt = new ConfigExt(c)
}
