package com.studiesandme.backend.common

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.util.ByteString
import com.google.inject.Inject
import spray.json._

class RestService @Inject() (apiSet: Set[RestComponent])
    extends Directives
    with RequestIdDirective
    with SprayJsonSupport {

  val timeoutResponse: HttpResponse = HttpResponse(
    StatusCodes.ServiceUnavailable,
    entity = HttpEntity(
      ContentTypes.`application/json`,
      ApiError(
        "error-1101",
        "The server was not able to produce a timely response to your request.\nPlease try again in a short while!",
      ).toJson.toString,
    ),
  )

  private def wrapToJson(response: HttpResponse): HttpResponse = {
    val newResponse = response.entity match {
      case HttpEntity.Strict(ContentTypes.`text/plain(UTF-8)`, data) if response.status.isFailure =>
        response.copy(
          entity =
            HttpEntity(ContentTypes.`application/json`, ApiError("error-0000", data.utf8String).toJson.toString()),
        )
      case HttpEntity.Strict(ContentTypes.`text/plain(UTF-8)`, data)
          if data == ByteString("OK") || data == ByteString("") =>
        response.copy(entity = HttpEntity.Strict(ContentTypes.`application/json`, ByteString("{}")))
      case _ =>
        response
    }

    newResponse
  }

  private val exceptionHandler = {
    ExceptionHandler {
      case _: Throwable => {
        complete(
          (
            StatusCodes.InternalServerError,
            ApiError(
              "error-1104",
              "Sorry, something went wrong in fulfilling your request. Please contact us at backend@studiesandme.com if the problem persists.",
            ),
          ),
        )
      }
    }
  }

  private val rejectionHandler =
    RejectionHandler
      .newBuilder()
      .handle {
        case MissingQueryParamRejection(param) =>
          complete(
            (StatusCodes.BadRequest, ApiError("error-1102", s"Request is missing required query parameter '$param'")),
          )
      }
      .handle {
        case AuthorizationFailedRejection =>
          complete(
            (StatusCodes.Forbidden, ApiError("error-1103", "Authenticated user not authorized to access object.")),
          )
      }
      .result()
      .withFallback(RejectionHandler.default)

  // NOTE: This should be fixed asap
  @SuppressWarnings(Array("TraversableHead"))
  def route(): Route =
    mapResponse(wrapToJson) {
      handleRejections(rejectionHandler) {
        handleExceptions(exceptionHandler) {
          withRequestTimeoutResponse(_ => timeoutResponse) {
            provideRequestId { _ =>
              apiSet.tail.foldLeft(apiSet.head.route) { (chain, next) =>
                concat(chain, next.route)
              }
            }
          }
        }
      }
    }
}
