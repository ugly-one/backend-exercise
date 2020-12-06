package com.studiesandme.backend.tasks

import java.sql.SQLIntegrityConstraintViolationException
import java.time.Instant

import com.google.inject.Inject
import com.studiesandme.backend.common.{DBComponent, DBEnv, Health, NewtypeSlick, SpecialExecutionTactics}
import com.studiesandme.backend.{RepositoryHealthCheck, StudiesAndMeMappers, StudiesAndMeRepository}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

trait TasksRepository { this: DBComponent =>
  def create(contact: Task): Future[Task]
  def list(): Future[List[Task]]
  def get(id: TaskId): Future[Option[Task]]
  def update(task: Task): Future[Task]
}

trait TasksTable extends StudiesAndMeMappers with NewtypeSlick {
  this: DBComponent =>

  implicit lazy val taskIdColumnType = deriveUUIDAsStringColumn(TaskId.apply)

  class TasksTable(tag: Tag) extends Table[Task](tag, "tasks") {
    val id          = column[TaskId]("id", O.PrimaryKey)
    val description = column[String]("description")
    val createdAt   = column[Instant]("createdAt")
    val modified    = column[Instant]("modified")
    val completed   = column[Boolean]("completed")

    def * =
      (id, description, createdAt, modified, completed)
        .mapTo[Task]
  }

  val allTasks = TableQuery[TasksTable]
}

class TasksRepositoryImpl @Inject() (val driver: JdbcProfile)(val dbEnv: DBEnv)
    extends StudiesAndMeRepository
    with TasksRepository
    with TasksTable
    with DBComponent
    with SpecialExecutionTactics
    with RepositoryHealthCheck {
  import driver.api._

  override def create(task: Task): Future[Task] =
    dbEnv.db
      .run {
        allTasks += task
      }
      .map {
        case 1 => task
      }
      .recoverWith {
        // this really should never happen
        case e: SQLIntegrityConstraintViolationException =>
          throw new RuntimeException(s"Error creating task: $e")
      }

  override def list(): Future[List[Task]] =
    dbEnv.db
      .run {
        allTasks.result
      }
      .map {
        case result @ _ => result.toList
      }

  override def get(id: TaskId): Future[Option[Task]] = 
    dbEnv.db
      .run {
        allTasks.filter(_.id === id).result.headOption
      }

  override def update(task: Task): Future[Task] = 
    dbEnv.db
      .run {
        val taskFromDB = for { t <- allTasks if t.id === task.id } yield t
        taskFromDB.update(task)
      }
      .map {
        case 1 => task
      }

  override def isHealthy: Future[Health] =
    isHealthy(dbEnv, "Business Contacts repo")
}
