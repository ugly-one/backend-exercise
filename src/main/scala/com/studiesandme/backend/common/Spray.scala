package com.studiesandme.backend.common

import spray.json.JsonFormat
import spray.json.DefaultJsonProtocol._

object Spray {

  import NewtypeSpray.Implicits._

  implicit val clientIdFormat:  JsonFormat[ClientId]  = NewtypeSpray.deriveJsonFormat(ClientId.apply)
  implicit val groupIdFormat:   JsonFormat[GroupId]   = NewtypeSpray.deriveJsonFormat(GroupId.apply)
  implicit val requestIdFormat: JsonFormat[RequestId] = NewtypeSpray.deriveJsonFormat(RequestId.apply)
  implicit val userIdFormat:    JsonFormat[UserId]    = NewtypeSpray.deriveJsonFormat(UserId.apply)
//  implicit val correlationIdFormat: JsonFormat[CorrelationId] = NewtypeSpray.deriveJsonFormat(CorrelationId.apply)
//  implicit val causationIdFormat:   JsonFormat[CausationId]   = NewtypeSpray.deriveJsonFormat(CausationId.apply)
  implicit val clusterIdFormat: JsonFormat[ClusterId] = NewtypeSpray.deriveJsonFormat(ClusterId.apply)

}
