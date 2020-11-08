package com.studiesandme.backend.common

import org.slf4j.MDC

import scala.concurrent.ExecutionContext

// From: http://code.hootsuite.com/logging-contextual-info-in-an-asynchronous-scala-application/
trait MDCPropagatingExecutionContext extends ExecutionContext {
  // name the self-type "self" so we can refer to it inside the nested class
  self =>

  override def prepare(): ExecutionContext = new ExecutionContext {
    // Save the call-site MDC state
    val context = MDC.getCopyOfContextMap

    def execute(r: Runnable): Unit =
      self.execute(new Runnable {
        def setContextMap(contextMap: java.util.Map[String, String]): Unit = {
          if (contextMap != null) {
            MDC.setContextMap(contextMap)
          } else {
            MDC.clear()
          }
        }

        def run(): Unit = {
          // Save the existing execution-site MDC state
          val oldContext = MDC.getCopyOfContextMap

          try {
            // Set the call-site MDC state into the execution-site MDC
            setContextMap(context)

            r.run()
          } finally {
            // Restore the existing execution-site MDC state
            setContextMap(oldContext)
          }
        }
      })

    def reportFailure(t: Throwable): Unit = self.reportFailure(t)
  }
}

/**
  * Wrapper around an existing ExecutionContext that makes it propagate MDC information.
  */
class MDCPropagatingExecutionContextWrapper(wrapped: ExecutionContext)
    extends ExecutionContext
    with MDCPropagatingExecutionContext {

  override def execute(r: Runnable): Unit = wrapped.execute(r)

  override def reportFailure(t: Throwable): Unit = wrapped.reportFailure(t)
}

object MDCPropagatingExecutionContextWrapper {
  def apply(wrapped: ExecutionContext): MDCPropagatingExecutionContextWrapper = {
    new MDCPropagatingExecutionContextWrapper(wrapped)
  }
}
