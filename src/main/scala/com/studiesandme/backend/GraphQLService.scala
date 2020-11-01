package com.studiesandme.backend

import scala.concurrent.Future
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.google.inject.Inject
import com.leoilab.concurrent.SpecialExecutionTactics
import com.leoilab.sensible.health.{Health, HealthCheck, Healthy}
import com.studiesandme.backend.tasks.{CreateTaskInput, Task}
import com.typesafe.scalalogging.StrictLogging

trait GraphQLService {
  def createTask(input: CreateTaskInput): Future[Task]
  def listTasks(): Future[List[Task]]
}

class GraphQLServiceImpl @Inject() (
    tasksService: TasksService,
) extends GraphQLService
    with HealthCheck
    with SpecialExecutionTactics
    with SprayJsonSupport
    with StrictLogging {

  override def isHealthy: Future[Health] = Future.successful(Healthy)

  override def createTask(input: CreateTaskInput): Future[Task] = {
    tasksService.create(input)
  }
  override def listTasks(): Future[List[Task]] = {
    tasksService.list()
  }
}
