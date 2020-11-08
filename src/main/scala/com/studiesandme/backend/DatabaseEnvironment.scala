package com.studiesandme.backend

import com.studiesandme.backend.common.{ConfigValues, DBEnv}
import javax.inject.Inject
import slick.jdbc.JdbcProfile

class DatabaseEnvironment @Inject() (val config: ConfigValues, val driver: JdbcProfile) extends DBEnv {
  override def configValues: ConfigValues = config
}
