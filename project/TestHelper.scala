import java.io.File

import sbt.internal.util.ManagedLogger

import scala.sys.process._
import scala.util.Try

object TestHelper {

  val dockerCompose = "docker-compose"

  def runDevelopmentStack(log: ManagedLogger): Unit = {
    log.info(s"(Re)Starting development stack")
    s"$dockerCompose down -t 0".!
    log.info("Waiting for development test stack to start.")
    s"$dockerCompose up -d waitdev".!
  }

  def runIntegrationTestStack(log: ManagedLogger): Unit = {
    stopIntegrationTestStack(log, writeToTestLog = false)

    s"$dockerCompose -f docker-compose.yml pull".! match {
      case 0 =>
      case _ =>
        log.error("Not able to pull docker dependencies - maybe you need to log in?")
        sys.exit(1)
    }

    log.info("Starting integration test stack.")
    val access_key_id: String =
      if ("aws configure get aws_access_key_id".! == 0) "aws configure get aws_access_key_id".!!
      else System.getenv("AWS_ACCESS_KEY_ID")
    val access_secret_key: String =
      if (("aws configure get aws_secret_access_key" ! ProcessLogger(_ => ())) == 0)
        "aws configure get aws_secret_access_key".!!
      else System.getenv("AWS_SECRET_ACCESS_KEY")
    Process(
      s"$dockerCompose up -d studiesandme-wait",
      None,
      "AWS_ACCESS_KEY_ID"     -> AWSAccessKeyID().get,
      "AWS_SECRET_ACCESS_KEY" -> AWSSecretAccessKey().get,
      "AWS_SESSION_TOKEN"     -> AWSSessionToken().get,
    ).! match {
      case 0 => log.info("Services reply on /status.")
      case _ =>
        log.error("Failed to start up stack.")
        sys.exit(1)
    }
  }

  def stopIntegrationTestStack(log: ManagedLogger, writeToTestLog: Boolean = true): Unit = {
    if (writeToTestLog) {
      writeTestStackLog(log)
    }
    log.info("Stopping integration test stack.")
    s"$dockerCompose down -t 0".!
    log.info("Stopped integration test stack.")
  }

  def writeTestStackLog(log: ManagedLogger): Unit = {
    log.info("Dumping logs to integration-test-docker.log.")
    (s"$dockerCompose logs --no-color" #> new File("integration-test-docker.log")).!
  }
  private abstract class AWSVariable(envVariableName: String) {
    def get: String = {
      Try(s"aws configure get ${envVariableName.toLowerCase}".!!)
        .getOrElse {
          Option(System.getenv(envVariableName)).getOrElse("")
        }
    }
  }

  private case class AWSAccessKeyID() extends AWSVariable("AWS_ACCESS_KEY_ID")
  private case class AWSSecretAccessKey() extends AWSVariable("AWS_SECRET_ACCESS_KEY")
  private case class AWSSessionToken() extends AWSVariable("AWS_SESSION_TOKEN")
}
