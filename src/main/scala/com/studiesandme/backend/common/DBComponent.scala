package com.studiesandme.backend.common

import slick.jdbc.JdbcProfile

trait DBComponent {
  val driver: JdbcProfile
}
