package com.studiesandme.backend

import com.leoilab.sensible.rest.{AppModule, PlatformApp}

object Main extends PlatformApp {
  override def appModule: AppModule = new MainModule
}
