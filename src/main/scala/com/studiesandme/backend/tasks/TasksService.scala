package com.studiesandme.backend

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.google.inject.Inject
import com.studiesandme.backend.common.{Health, HealthCheck, Healthy, SpecialExecutionTactics}
import com.studiesandme.backend.tasks._
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.Future
import java.time.Instant
import scala.util.Success
import scala.util.Failure

trait TasksService {
  def create(contact: CreateTaskInput): Future[Task]
  def list(): Future[List[Task]]
  def complete(id: CompleteTaskInput): Future[Task]
  def delete(id: CompleteTaskInput): Future[TaskId]
}

class TasksServiceImpl @Inject() (
    tasksRepository: TasksRepository,
) extends TasksService
    with HealthCheck
    with SpecialExecutionTactics
    with SprayJsonSupport
    with StrictLogging {

  override def isHealthy: Future[Health] = Future.successful(Healthy)

  override def create(input: CreateTaskInput): Future[Task] = {
    val newTask = Task.fromInput(input)
    tasksRepository.create(newTask)
  }

  override def list(): Future[List[Task]] = {
    tasksRepository.list()
  }

  override def complete(id: CompleteTaskInput): Future[Task] = {
      for {
        task <- tasksRepository.get(id.id)
        updatedTask <- task match {
          case Some(t) => {
            tasksRepository.update(t.copy(completed = true))
          }
          case None => throw new Exception("no task with given id")
        }
      } yield updatedTask
    }

  override def delete(id: CompleteTaskInput): Future[TaskId] = {
    tasksRepository.delete(id.id)
  }
}
