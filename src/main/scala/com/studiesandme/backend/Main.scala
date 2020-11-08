package com.studiesandme.backend

import com.studiesandme.backend.common.{AppModule, PlatformApp}

object Main extends PlatformApp {
  override def appModule: AppModule = new MainModule
}
