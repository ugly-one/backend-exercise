package com.studiesandme.backend.common

import java.util.UUID

import org.joda.time.{DateTime, Duration}
import pdi.jwt.{Jwt, JwtAlgorithm}
import spray.json._

import scala.util.Try

trait AuthSupport {

  object AccessTokenHelper {
    private val secretKey  = "test-secret"
    private val algorithms = Seq(JwtAlgorithm.HS256)

    @SuppressWarnings(Array("OptionGet"))
    def encode(authInfo: AuthInfo): AccessToken = {
      // will not throw None.get, since test AccessToken string is built from AuthInfo
      AccessToken.fromString(Jwt.encode(authInfo.toJson.toString, secretKey, JwtAlgorithm.HS256)).get
    }

    def decode(accessToken: AccessToken): Try[AuthInfo] = {
      Jwt.decodeRaw(accessToken.raw, secretKey, algorithms).map(_.parseJson.convertTo[AuthInfo])
    }

  }

  def generateTestAuthInfo(isAdmin: Boolean = false): AuthInfo = {
    AuthInfo(
      UserId(UUID.randomUUID()),
      ClientId("test"),
      Some(GroupId(UUID.randomUUID())),
      Some(isAdmin),
      Some(DateTime.parse(DateTime.now.toLocalDate.toString)),
      Some(Duration.standardDays(2)),
    )
  }

  def generateTestAccessToken(isAdmin: Boolean = false): AccessToken = {
    generateTestAccessToken(generateTestAuthInfo(isAdmin))
  }

  def generateTestAccessToken(authInfo: AuthInfo): AccessToken = {
    AccessTokenHelper.encode(authInfo)
  }
}
