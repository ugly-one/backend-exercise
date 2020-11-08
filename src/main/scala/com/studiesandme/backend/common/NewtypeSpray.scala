package com.studiesandme.backend.common

import java.util.UUID
import spray.json.{DeserializationException, JsString, JsValue, JsonFormat, RootJsonFormat}
import scala.util.{Failure, Success, Try}

object NewtypeSpray {

  object Implicits {

    implicit val uuidFormat: JsonFormat[UUID] = new JsonFormat[UUID] {
      def write(uuid: UUID): JsValue = JsString(uuid.toString)
      def read(json:  JsValue): UUID = {
        json match {
          case JsString(rawUUID) =>
            Try(UUID.fromString(rawUUID)) match {
              case Success(uuid)  => uuid
              case Failure(error) => throw new DeserializationException("Invalid UUID format", error)
            }
          case _ => throw new DeserializationException("Trying to convert non JsString to UUID")
        }
      }
    }

  }

  def deriveJsonFormat[T, NT <: Newtype[T]](
      constructor:        T => NT,
  )(implicit valueFormat: JsonFormat[T]): JsonFormat[NT] = {
    new JsonFormat[NT] {
      def write(newtype: NT):      JsValue = valueFormat.write(newtype.value)
      def read(json:     JsValue): NT      = constructor(valueFormat.read(json))
    }
  }

  def deriveRootJsonFormat[T, NT <: Newtype[T]](
      constructor:        T => NT,
  )(implicit valueFormat: JsonFormat[T]): RootJsonFormat[NT] = {
    new RootJsonFormat[NT] {
      def write(newtype: NT):      JsValue = valueFormat.write(newtype.value)
      def read(json:     JsValue): NT      = constructor(valueFormat.read(json))
    }
  }
}
