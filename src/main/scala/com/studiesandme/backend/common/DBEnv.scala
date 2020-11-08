package com.studiesandme.backend.common

trait DBEnv extends DBComponent {

  import driver.api._

  def configValues: ConfigValues

  val clientCluster: Map[ClientId, ClusterId] =
    Map.empty.withDefaultValue(Clusters.Cluster1)

  val clusterIds: Seq[ClusterId] = Clusters.active

  lazy val dbClusters: Map[ClusterId, Database] = clusterIds.map { clusterId =>
    val c = configValues.Db.connectionConfig(clusterId)
    clusterId -> Database.forURL(
      c.url,
      c.user,
      c.password,
      executor =
        AsyncExecutor(s"AsyncExecutor.${clusterId.value}", c.maxConnections, c.maxConnections, 1000, c.maxConnections),
    )
  }.toMap

  def db(implicit clientId: ClientId): Database = {
    dbClusters(clientCluster(clientId))
  }
}
