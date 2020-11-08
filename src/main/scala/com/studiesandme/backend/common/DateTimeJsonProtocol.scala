package com.studiesandme.backend.common

import org.joda.time.format.ISODateTimeFormat
import org.joda.time.{DateTime, Duration}
import spray.json.{DefaultJsonProtocol, JsString, JsValue, RootJsonFormat, _}

object DateTimeJsonProtocol extends DefaultJsonProtocol {

  implicit object DateTimeFormat extends RootJsonFormat[DateTime] {

    val formatter = ISODateTimeFormat.dateTimeNoMillis

    def write(obj: DateTime): JsValue = {
      JsString(formatter.print(obj))
    }

    @SuppressWarnings(Array("CatchThrowable"))
    def read(json: JsValue): DateTime = json match {
      case JsString(s) =>
        try {
          formatter.parseDateTime(s)
        } catch {
          case _: Throwable => error(s)
        }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): DateTime = {
      val example = formatter.print(0)
      deserializationError(f"'$v' is not a valid date value. Dates must be in compact ISO-8601 format, e.g. '$example'")
    }
  }

  implicit object DurationFormat extends RootJsonFormat[Duration] {
    def write(obj: Duration): JsValue = {
      JsString(obj.getStandardSeconds.toString)
    }

    @SuppressWarnings(Array("CatchThrowable"))
    def read(json: JsValue): Duration = json match {
      case JsString(s) =>
        try {
          Duration.standardSeconds(s.toLong)
        } catch {
          case _: Throwable => error(s)
        }
      case _ =>
        error(json.toString())
    }

    def error(v: Any): Duration = {
      deserializationError(f"'$v' is not a valid duration. Durations must be an integer.")
    }
  }
}
