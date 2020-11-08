package com.studiesandme.backend.common

import org.joda.time.{DateTime, Duration}
import spray.json.DefaultJsonProtocol

final case class AuthInfo(
    userId:    UserId,
    clientId:  ClientId,
    groupId:   Option[GroupId] = None,
    isAdmin:   Option[Boolean] = Some(false),
    issuedAt:  Option[DateTime] = None,
    expiresIn: Option[Duration] = None,
)

object AuthInfo extends DefaultJsonProtocol {
  import com.studiesandme.backend.common.Spray._
  import com.studiesandme.backend.common.DateTimeJsonProtocol._

  implicit val authClaimFormat = jsonFormat6(AuthInfo.apply)
}
