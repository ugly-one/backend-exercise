package com.studiesandme.backend

import com.google.inject.{Provides, Singleton}
import com.leoilab.db.DBEnv
import com.leoilab.sensible.rest.AppModule
import slick.jdbc.JdbcProfile

class MainModule extends AppModule {
  override def configure(): Unit = {
    bind[DBEnv].to[DatabaseEnvironment].in[Singleton]

    install(new RepositoriesModule)
    install(new ServicesModule)

    bind[GraphQl].to[GraphQlImpl]
    bindApi.to[ApiImpl]

    install(new HealthModule)
    ()
  }

  @Provides
  def providesJdbcProfile(): JdbcProfile = slick.jdbc.MySQLProfile
}
