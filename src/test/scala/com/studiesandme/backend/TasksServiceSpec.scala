package com.studiesandme.backend

import com.studiesandme.backend.common.StandardSpec
import com.studiesandme.backend.common.UnitTestSupport
import com.studiesandme.backend.tasks.TasksRepository
import com.studiesandme.backend.tasks.Task
import scala.concurrent.Future
import com.studiesandme.backend.tasks.TaskId
import java.time.Instant
import org.mockito.Mockito._
import com.studiesandme.backend.tasks.TaskIdInput
import com.studiesandme.backend.tasks.CreateTaskInput
import org.mockito.ArgumentMatchers.{any, argThat}
import org.mockito.AdditionalAnswers.{returnsFirstArg}

class TasksServiceSpec extends StandardSpec with UnitTestSupport {
    var repository: TasksRepository = _
    var service: TasksService = _

    before {
      repository = mock[TasksRepository]
      service = new TasksServiceImpl(repository)
    }

    it must "return the same data as repository" in {
      val tasksFromRepo = List(Task.apply(TaskId.generate, "some task", Instant.now(), Instant.now(), false))
      when(repository.list())
        .thenReturn(Future(tasksFromRepo))

      val tasks = service.list().futureValue

      verify(repository).list()
      tasks shouldBe tasksFromRepo
    }

    it must "throw exception when asked to delete a non-existing task" in {
      val taskId = TaskId.generate
      when(repository.get(taskId))
        .thenReturn(Future(None))

      intercept[Exception]{
        val result = service.delete(TaskIdInput(taskId)).futureValue
      }
    }

    it must "invoke delete on repository when asked to delete existing task" in {
      val taskId = TaskId.generate
      when(repository.get(taskId))
        .thenReturn(Future(Some(Task.fromInput(CreateTaskInput("some task")))))

      val result = service.delete(TaskIdInput(taskId)).futureValue

      verify(repository).delete(taskId)
    }

    it must "invoke repository with the new description" in {
      val taskId = TaskId.generate
      val task = Task.fromInput((CreateTaskInput("some task")))
      when(repository.get(taskId))
        .thenReturn(Future(Some(task)))
      when(repository.update(any[Task]()))
        .thenReturn(Future(task)) // task returned from the mock is not important
      val newDescription = "new"
      val result = service.updateDescription(taskId, newDescription).futureValue

      verify(repository)
        .update(task.copy(description = newDescription))
    }
}
