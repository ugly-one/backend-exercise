package com.studiesandme.backend

import spray.json.{DefaultJsonProtocol, JsValue, JsonFormat, RootJsonFormat}

case class ErrorCodeDTO(code: String)

object ErrorCodeDTO extends DefaultJsonProtocol {
  implicit val errorCodeDTOFormat: RootJsonFormat[ErrorCodeDTO] = jsonFormat1(ErrorCodeDTO.apply)
}

case class ErrorDTO(message: String, extensions: Option[ErrorCodeDTO])

object ErrorDTO extends StudiesAndMeJsonFormatters {
  implicit val errorDTOFormat: RootJsonFormat[ErrorDTO] = jsonFormat2(ErrorDTO.apply)
  implicit val errorDTOListFormat: AnyRef with RootJsonFormat[List[ErrorDTO]] =
    listFormat(errorDTOFormat)
}

case class QueryRequestDTO(query: String, variables: Option[JsValue], operationName: Option[String])

object QueryRequestDTO extends StudiesAndMeJsonFormatters {
  implicit val graphqlQueryFormat: RootJsonFormat[QueryRequestDTO] =
    jsonFormat3(QueryRequestDTO.apply)
}

case class QueryResponseDTO[T](data: Option[T], errors: Option[List[ErrorDTO]])

object QueryResponseDTO extends StudiesAndMeJsonFormatters {
  implicit def queryResponseDTOFormat[A: JsonFormat]: RootJsonFormat[QueryResponseDTO[A]] =
    jsonFormat2(QueryResponseDTO.apply)
}
