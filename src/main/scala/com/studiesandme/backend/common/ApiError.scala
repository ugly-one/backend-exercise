package com.studiesandme.backend.common

import spray.json.DefaultJsonProtocol

final case class ApiError(code: String, msg: String, errorContext: Option[String] = None)

object ApiError extends DefaultJsonProtocol {
  implicit val apiErrorFormat = jsonFormat3(ApiError.apply)
}
