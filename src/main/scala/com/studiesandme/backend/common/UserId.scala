package com.studiesandme.backend.common

import java.util.UUID

final case class UserId(value: UUID) extends Newtype[UUID] {
  override def toString: String = value.toString
}

object UserId {
  def generate: UserId = UserId(UUID.randomUUID())
}
