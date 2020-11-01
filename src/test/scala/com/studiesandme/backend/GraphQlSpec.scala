package com.studiesandme.backend

import com.studiesandme.backend.tasks._
import com.leoilab.testkit.{StandardSpec, UnitTestSupport}
import sangria.schema.Schema
import spray.json.DefaultJsonProtocol
import graphient.{GraphqlCall, Query, QueryGenerator}
import sangria.ast.Document
import org.mockito.Mockito._
import spray.json._

import scala.concurrent.Future

class GraphQlSpec extends StandardSpec with DefaultJsonProtocol with UnitTestSupport with TasksGraphQLSchema {
  var graphQl: GraphQl        = _
  var service: GraphQLService = _

  before {
    service = mock[GraphQLService]
    graphQl = new GraphQlImpl(service)
  }

  val schema = Schema(query = SchemaDefinition.QueryType, mutation = Some(SchemaDefinition.MutationType))

  it must "handle 'listTasks query" in {
    val taskList: List[Task] = List()
    when(service.listTasks())
      .thenReturn(Future.successful(taskList))
    val query = generateQuery(Query(TaskQueries.tasks()))

    val result = graphQl.executeGraphQlQuery(query, None, None).futureValue

    result._2 shouldBe QueryResponseDTO(Some(JsObject("tasks" -> taskList.toJson)), None).toJson
    verify(service).listTasks()
  }

  private def generateQuery[Ctx, T](query: GraphqlCall[Ctx, T]): Document = {
    new QueryGenerator(schema).generateQuery(query).right.get
  }
}
