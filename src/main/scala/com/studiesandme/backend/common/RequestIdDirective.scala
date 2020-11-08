package com.studiesandme.backend.common

import java.util.UUID

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.server.{Directive1, Directives}
import org.slf4j.MDC

trait RequestIdDirective extends Directives {

  def provideRequestId: Directive1[RequestId] = {

    def registerRequestId(requestId: RequestId): Directive1[RequestId] = {
      MDC.put("requestId", requestId.toString)
      provide(requestId)
    }

    optionalHeaderValueByName("X-Request-Id").flatMap {
      case Some(value) =>
        registerRequestId(RequestId(UUID.fromString(value)))
      case None =>
        val requestId: RequestId = RequestId(UUID.randomUUID())
        mapRequest(r => r.withHeaders(r.headers :+ RawHeader("X-Request-Id", requestId.toString))) & registerRequestId(
          requestId,
        )
    }
  }

}
