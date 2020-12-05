package com.studiesandme.backend

import java.util.UUID

import com.studiesandme.backend.common.Newtype
import com.studiesandme.backend.common.NewtypeSpray.Implicits._
import com.studiesandme.backend.common.NewtypeSpray.deriveJsonFormat
import spray.json.RootJsonFormat

package tasks {

  import java.time.Instant

  final case class TaskId(value: UUID) extends Newtype[UUID] {
    override def toString: String = value.toString
  }
  object TaskId {
    implicit val taskIdFormat = deriveJsonFormat(TaskId.apply)
    def generate: TaskId = TaskId(UUID.randomUUID())
  }

  final case class CreateTaskInput(
      description: String,
  )
  object CreateTaskInput extends StudiesAndMeJsonFormatters {
    implicit val createTaskInputFormat: RootJsonFormat[CreateTaskInput] =
      jsonFormat1(CreateTaskInput.apply)
  }

  final case class Task(
      id:          TaskId,
      description: String,
      createdAt:   Instant,
      modifiedAt:  Instant,
      completed:   Boolean,
  )
  object Task extends StudiesAndMeJsonFormatters {
    def tupled = (Task.apply _).tupled

    implicit val taskFormat: RootJsonFormat[Task] =
      jsonFormat5(Task.apply)

    def fromInput(input: CreateTaskInput): Task = {
      Task(
        id          = TaskId.generate,
        description = input.description,
        createdAt   = Instant.now(),
        modifiedAt  = Instant.now(),
        completed   = false,
      )
    }
  }

}
