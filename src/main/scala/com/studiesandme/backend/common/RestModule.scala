package com.studiesandme.backend.common

import net.codingwell.scalaguice.ScalaModule

/**
  * Adds HTTP server functionality and installs the ApisModule which registers
  * the REST APIs defined by the application.
  */
class RestModule extends ScalaModule {
  override def configure() = {
    bind[RestServer]
    ()
  }
}
