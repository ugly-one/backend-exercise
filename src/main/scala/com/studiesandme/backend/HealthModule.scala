package com.studiesandme.backend

import com.leoilab.sensible.rest.AppModule
import com.studiesandme.backend.tasks.TasksRepositoryImpl

class HealthModule extends AppModule {
  override def configure(): Unit = {
    bindHealthComponent.to[GraphQLServiceImpl]
    bindHealthComponent.to[TasksServiceImpl]
    bindHealthComponent.to[TasksRepositoryImpl]
    ()
  }
}
