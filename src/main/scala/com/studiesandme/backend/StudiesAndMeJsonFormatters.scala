package com.studiesandme.backend

import java.time.Instant

import com.leoilab.newtype.NewtypeSpray.Implicits._
import com.leoilab.newtype.NewtypeSpray.deriveJsonFormat
import com.studiesandme.backend.tasks.TaskId
import spray.json.{deserializationError, DefaultJsonProtocol, JsNumber, JsValue, JsonFormat}

trait StudiesAndMeJsonFormatters extends DefaultJsonProtocol {
  implicit val taskIdFormat = deriveJsonFormat(TaskId.apply)

  implicit object InstantJsonFormat extends JsonFormat[Instant] {
    def write(x:    Instant) = JsNumber(x.toEpochMilli)
    def read(value: JsValue) = value match {
      case JsNumber(x) => Instant.ofEpochMilli(x.longValue())
      case x =>
        deserializationError("Expected Time Instant as JsNumber, but got " + x)
    }

  }
}
