package com.studiesandme.backend.common

final case class ClusterId(value: String) extends Newtype[String] {
  override def toString: String = value
}
