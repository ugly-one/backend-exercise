package com.studiesandme.backend.common

import java.util.UUID
import scala.reflect.ClassTag

trait NewtypeSlick { this: DBComponent =>

  import driver.api._

  def deriveStringColumn[NT <: Newtype[String]: ClassTag](constructor: String => NT) = {
    driver.api.MappedColumnType.base[NT, String](_.value, constructor)
  }

  def deriveIntColumn[NT <: Newtype[Int]: ClassTag](constructor: Int => NT) = {
    driver.api.MappedColumnType.base[NT, Int](_.value, constructor)
  }

  def deriveLongColumn[NT <: Newtype[Long]: ClassTag](constructor: Long => NT) = {
    driver.api.MappedColumnType.base[NT, Long](_.value, constructor)
  }

  def deriveDoubleColumn[NT <: Newtype[Double]: ClassTag](constructor: Double => NT) = {
    driver.api.MappedColumnType.base[NT, Double](_.value, constructor)
  }

  def deriveUUIDAsStringColumn[NT <: Newtype[UUID]: ClassTag](constructor: UUID => NT) = {
    driver.api.MappedColumnType.base[NT, String](_.value.toString, raw => constructor(UUID.fromString(raw)))
  }

}
