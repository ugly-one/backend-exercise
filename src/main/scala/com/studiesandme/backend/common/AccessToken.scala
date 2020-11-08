package com.studiesandme.backend.common

import pdi.jwt.JwtBase64
import scala.util.Try
import spray.json._

final case class AccessToken private (
    raw:      String,
    authInfo: AuthInfo,
) {
  override def toString: String = raw
}

object AccessToken {

  def fromString(raw: String): Option[AccessToken] = {
    parseAuthInfo(raw).map(AccessToken(raw, _))
  }

  private def parseAuthInfo(raw: String): Option[AuthInfo] = {
    raw.split('.') match {
      case Array(_, encodedPayload, _) =>
        Try(JwtBase64.decodeString(encodedPayload).parseJson.convertTo[AuthInfo]).toOption
      case _ => None
    }
  }

  implicit val accessTokenFormat: JsonFormat[AccessToken] = new JsonFormat[AccessToken] {
    def read(json: JsValue): AccessToken = {
      json match {
        case JsString(raw) =>
          AccessToken.fromString(raw) match {
            case None        => throw DeserializationException("Invalid AccessToken")
            case Some(token) => token
          }
        case _ => throw DeserializationException("Invalid json format for AccessToken(expected String)")
      }
    }
    def write(token: AccessToken): JsValue = JsString(token.raw)
  }

}
