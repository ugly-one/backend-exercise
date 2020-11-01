package com.studiesandme.backend

import com.leoilab.config.ConfigValues
import com.leoilab.db.DBEnv
import javax.inject.Inject
import slick.jdbc.JdbcProfile

class DatabaseEnvironment @Inject() (val config: ConfigValues, val driver: JdbcProfile) extends DBEnv {
  override def configValues: ConfigValues = config
}
