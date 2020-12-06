package com.studiesandme.backend.tasks

import com.studiesandme.backend.common.SpecialExecutionTactics
import sangria.macros.derive.deriveInputObjectType
import com.studiesandme.backend.{BaseGraphQLSchema, GraphQLService}
import sangria.marshalling.sprayJson._
import sangria.schema.{fields, Argument, Field, InputObjectType, ListType, LongType, ObjectType, StringType, BooleanType}

trait TasksGraphQLSchema extends BaseGraphQLSchema with SpecialExecutionTactics {
  import ScalarHelpers._

  val TaskType = ObjectType[Unit, Task](
    name = "task",
    fields[Unit, Task](
      Field("id", TaskIdType, resolve          = _.value.id),
      Field("description", StringType, resolve = _.value.description),
      Field(
        "createdAt",
        LongType,
        resolve = _.value.createdAt.getEpochSecond(),
      ),
      Field(
        "modifiedAt",
        LongType,
        resolve = _.value.modifiedAt.getEpochSecond(),
      ),
      Field("completed", BooleanType, resolve = _.value.completed),
    ),
  )

  implicit val CreateTaskInputType: InputObjectType[CreateTaskInput] =
    deriveInputObjectType[CreateTaskInput]()
  val TaskIdArg = Argument("id", TaskIdType)
  val CreateTaskInputArg =
    Argument("input", CreateTaskInputType)

  implicit val CompleteTaskInputType: InputObjectType[TaskIdInput] = 
    deriveInputObjectType[TaskIdInput]()
  val CompleteTaskInputArg = Argument("id", CompleteTaskInputType)

  object TaskQueries {
    def tasks(): Field[GraphQLService, Unit] = Field(
      "tasks",
      ListType(TaskType),
      description = Some("Returns all tasks"),
      resolve = c =>
        for {
          result <- c.ctx.listTasks()
        } yield result,
    )
  }

  object TaskMutations {
    def createTask(): Field[GraphQLService, Unit] = Field(
      "createTask",
      TaskType,
      description = Some("Create new task"),
      arguments   = CreateTaskInputArg :: Nil,
      resolve     = c => c.ctx.createTask(c.arg(CreateTaskInputArg)),
    )
    def completeTask(): Field[GraphQLService, Unit] = Field(
      "completeTask",
      TaskType,
      description = Some("Complete a task"),
      arguments   = CompleteTaskInputArg :: Nil,
      resolve     = c => c.ctx.completeTask(c.arg(CompleteTaskInputArg)),
    )
    def deleteTask(): Field[GraphQLService, Unit] = Field(
      "deleteTask",
      TaskType,
      description = Some("Delete a task"),
      arguments   = CompleteTaskInputArg :: Nil,
      resolve     = c => c.ctx.deleteTask(c.arg(CompleteTaskInputArg)),
    )
  }
}
