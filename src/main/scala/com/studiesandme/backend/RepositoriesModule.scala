package com.studiesandme.backend

import com.google.inject.Singleton
import com.leoilab.sensible.rest.AppModule
import com.studiesandme.backend.tasks.{TasksRepository, TasksRepositoryImpl}

class RepositoriesModule extends AppModule {
  override def configure(): Unit = {
    bind[TasksRepository]
      .to[TasksRepositoryImpl]
      .in[Singleton]
  }
}
