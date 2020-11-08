package com.studiesandme.backend.common

import java.util.UUID

final case class RequestId(value: UUID) extends Newtype[UUID] {
  override def toString: String = value.toString
}

object RequestId {
  def generate: RequestId = RequestId(UUID.randomUUID())
}
