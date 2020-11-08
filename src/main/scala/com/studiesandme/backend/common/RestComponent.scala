package com.studiesandme.backend.common

import akka.http.scaladsl.server.Route

trait RestComponent {
  def route: Route
}
