package com.studiesandme.backend.common

final case class ClientId(value: String) extends Newtype[String] {
  override def toString: String = value
}
