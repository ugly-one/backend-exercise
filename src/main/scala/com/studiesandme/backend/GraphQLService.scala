package com.studiesandme.backend

import scala.concurrent.Future
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.google.inject.Inject
import com.studiesandme.backend.common.{Health, HealthCheck, Healthy, SpecialExecutionTactics}
import com.studiesandme.backend.tasks.{CreateTaskInput, Task, TaskIdInput}
import com.typesafe.scalalogging.StrictLogging
import com.studiesandme.backend.tasks.TaskId

trait GraphQLService {
  def createTask(input: CreateTaskInput): Future[Task]
  def listTasks(): Future[List[Task]]
  def completeTask(id: TaskIdInput): Future[Task]
  def deleteTask(id: TaskIdInput): Future[Task]
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
  override def completeTask(id: TaskIdInput): Future[Task] = {
    tasksService.complete(id)
  }
  override def deleteTask(id: TaskIdInput): Future[Task] = {
    tasksService.delete(id)
  }
}
