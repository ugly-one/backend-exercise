package com.studiesandme.backend

import com.studiesandme.backend.common.AppModule
import com.studiesandme.backend.tasks.TasksRepositoryImpl

class HealthModule extends AppModule {
  override def configure(): Unit = {
    bindHealthComponent.to[GraphQLServiceImpl]
//    bindHealthComponent.to[TasksServiceImpl]
    bindHealthComponent.to[TasksRepositoryImpl]
    ()
  }
}
