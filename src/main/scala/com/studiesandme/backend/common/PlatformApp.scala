package com.studiesandme.backend.common

import com.google.inject.Guice
import com.typesafe.scalalogging.StrictLogging

abstract class PlatformApp extends App with StrictLogging with SpecialExecutionTactics {
  def appModule: AppModule

  val injector = Guice.createInjector(new PlatformMainModule(appModule))

  /* Run schema migrations before starting the app. Has distributed lock. */
  val ms = injector.getInstance(classOf[MigrationService])

  /* Start the rest server */
  injector.getInstance(classOf[RestServer]).start
}
