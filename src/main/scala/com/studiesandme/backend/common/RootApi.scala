package com.studiesandme.backend.common

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives

class RootApi extends RestComponent with Directives {

  val route =
    pathEndOrSingleSlash {
      get {
        complete(StatusCodes.OK)
      }
    }
}
