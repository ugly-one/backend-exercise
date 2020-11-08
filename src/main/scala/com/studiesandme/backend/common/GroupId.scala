package com.studiesandme.backend.common

import java.util.UUID

final case class GroupId(value: UUID) extends Newtype[UUID] {
  override def toString: String = value.toString
}

object GroupId {
  def generate: GroupId = GroupId(UUID.randomUUID())
}
