package com.studiesandme.backend.common

trait SpecialExecutionTactics {

  implicit val ec = com.studiesandme.backend.common.Execution.defaultContext

}
