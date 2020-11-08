package com.studiesandme.backend

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import akka.http.scaladsl.server.Route
import com.studiesandme.backend.tasks.{CreateTaskInput, Task}
import com.studiesandme.backend.common.{
  AccessToken,
  AuthInfo,
  AuthSupport,
  Clients,
  StandardSpec,
  UnitTestSupport,
  UserId,
}
import sangria.ast.Document
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers.any
import spray.json._

import scala.concurrent.Future

class ApiSpec extends StandardSpec with UnitTestSupport with SprayJsonSupport with AuthSupport {
  var graphQl: GraphQl = _

  before {
    graphQl = mock[GraphQl]
  }

  implicit val authInfo:    AuthInfo    = AuthInfo(UserId.generate, Clients.Test)
  implicit val accessToken: AccessToken = generateTestAccessToken(authInfo)

  val addAuthorizationHeader = new RequestTransformer {
    def apply(request: HttpRequest): HttpRequest =
      request ~> addHeader("Authorization", s"Bearer $accessToken")
  }

  def testRoute: Route = new ApiImpl(graphQl).route

  "POST /graphql" must "return OK on a valid query" in {
    val queryPayload     = QueryRequestDTO("query { foo { bar } }", None, None)
    val dummyResponse    = Task.fromInput(CreateTaskInput(description = "dummy"))
    val queryResponseDTO = QueryResponseDTO(Some(List(dummyResponse)), None)
    when(
      graphQl.executeGraphQlQuery(
        any[Document],
        any[Option[String]],
        any[Option[JsValue]],
      ),
    ).thenReturn(Future.successful(StatusCodes.OK -> queryResponseDTO.toJson))

    Post("/graphql", queryPayload) ~> addAuthorizationHeader ~> testRoute ~> check {
      status should be(StatusCodes.OK)
      verify(graphQl).executeGraphQlQuery(
        any[Document],
        any[Option[String]],
        any[Option[JsValue]],
      )
    }
  }

  it should "allow unauthorized queries" in {
    val queryPayload     = QueryRequestDTO("query { foo { bar } }", None, None)
    val dummyResponse    = Task.fromInput(CreateTaskInput(description = "dummy"))
    val queryResponseDTO = QueryResponseDTO(Some(List(dummyResponse)), None)
    when(
      graphQl.executeGraphQlQuery(
        any[Document],
        any[Option[String]],
        any[Option[JsValue]],
      ),
    ).thenReturn(Future.successful(StatusCodes.OK -> queryResponseDTO.toJson))

    Post("/graphql", queryPayload) ~> testRoute ~> check {
      status should be(StatusCodes.OK)
      verify(graphQl).executeGraphQlQuery(any[Document], any[Option[String]], any[Option[JsValue]])
    }
  }
}
