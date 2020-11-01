package com.studiesandme.backend

import net.codingwell.scalaguice.ScalaModule

class ServicesModule extends ScalaModule {
  override def configure: Unit = {
    bind[TasksService].to[TasksServiceImpl]
    bind[GraphQLService].to[GraphQLServiceImpl]
    ()
  }
}
