package com.studiesandme.backend

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.google.inject.Inject
import com.studiesandme.backend.common.{AuthDirectives, RestComponent, SpecialExecutionTactics}
import sangria.parser.DeliveryScheme.Try
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString}

import scala.util.{Failure, Success}

class ApiImpl @Inject() (graphQl: GraphQl)
    extends RestComponent
    with SpecialExecutionTactics
    with StudiesAndMeJsonFormatters
    with SprayJsonSupport
    with AuthDirectives {

  def route: Route = {
    concat(
      path("graphql") {
        concat(
          post {
            entity(as[QueryRequestDTO]) { queryDto =>
              QueryParser.parse(queryDto.query) match {
                case Success(queryAst) =>
                  complete(
                    graphQl.executeGraphQlQuery(queryAst, queryDto.operationName, queryDto.variables),
                  )
                case Failure(error) =>
                  complete((StatusCodes.BadRequest, JsObject("error" -> JsString(error.getMessage))))
              }
            }
          },
          options { complete(StatusCodes.OK) },
        )
      },
      path("graphiql") {
        getFromResource("graphiql.html")
      },
    )
  }
}
