package com.studiesandme.backend.common

import net.codingwell.scalaguice.{ScalaModule, ScalaMultibinder}

abstract class AppModule extends ScalaModule {
  private lazy val apiBinder = ScalaMultibinder.newSetBinder[RestComponent](binder)

  protected[this] def bindApi: ScalaModule.ScalaLinkedBindingBuilder[RestComponent] = {
    apiBinder.addBinding
  }

  private lazy val healthBinder = ScalaMultibinder.newSetBinder[HealthCheck](binder)

  protected[this] def bindHealthComponent: ScalaModule.ScalaLinkedBindingBuilder[HealthCheck] = {
    healthBinder.addBinding
  }
}
