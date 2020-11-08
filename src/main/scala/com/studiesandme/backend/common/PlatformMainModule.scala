package com.studiesandme.backend.common

class PlatformMainModule(appModule: AppModule) extends AppModule {
  override def configure() = {
    install(new AkkaModule)
    install(new ConfigModule)
    install(new DatabaseModule)
    install(new RestModule)

    bindApi.to[StatusApi]
    bindApi.to[RootApi]
    install(appModule)
  }
}
