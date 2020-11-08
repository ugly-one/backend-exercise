package com.studiesandme.backend.common

final case class ConnectionConfig(url: String, user: String, password: String, maxConnections: Int = 20)
