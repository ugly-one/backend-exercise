package com.studiesandme.backend.common

import net.codingwell.scalaguice.ScalaModule

class DatabaseModule extends ScalaModule {
  override def configure(): Unit = {
    bind[MigrationService].to[MigrationServiceImpl]
    ()
  }
}
