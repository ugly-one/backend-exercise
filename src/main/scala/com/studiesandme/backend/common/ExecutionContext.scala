package com.studiesandme.backend.common

import scala.concurrent.ExecutionContext

object Execution {

  def defaultContext: ExecutionContext = MDCPropagatingExecutionContextWrapper(ExecutionContext.Implicits.global)

}
