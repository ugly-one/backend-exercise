import sbt.Resolver

enablePlugins(DockerPlugin)

val leoSnapshots = "s3://leo-platform-packages/snapshots"
val leoReleases  = "s3://leo-platform-packages/releases"

lazy val bootdb          = taskKey[Unit]("Boot dependencies for the application")
lazy val IntegrationTest = config("it") extend Test

lazy val dockerSettings = dockerfile in docker := {
  val appName = name.value

  val appDirTarget: String = s"/opt/$appName"
  val libDirTarget: File   = file(s"$appDirTarget/lib")
  val binDirTarget: File   = file(s"$appDirTarget/bin")

  val runScriptName:   String = "runapp.sh"
  val runScript:       File   = baseDirectory(_ / "runapp.sh").value
  val runScriptTarget: File   = file(s"$binDirTarget/$runScriptName")

  val libDir: File = pack.value / "lib"

  new Dockerfile {
    from("085816956471.dkr.ecr.eu-west-1.amazonaws.com/com.leoilab/base:v2.2")

    add(runScript, runScriptTarget)
    run("chmod", "+x", runScriptTarget.toString)

    add(libDir, libDirTarget)

    expose(9103)

    cmd(runScriptTarget.toString)
  }
}

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        organization := "com.studiesandme",
        scalaVersion := "2.12.7",
        scalacOptions := Seq(
          "-unchecked",
          "-feature",
          "-deprecation",
          "-encoding",
          "utf8",
          "-language:postfixOps",
          "-language:implicitConversions",
          "-Ypartial-unification",
        ),
      ),
    ),
    name := "studiesandme-backend-exercise",
    libraryDependencies ++= {
      val akkaHttpV = "10.1.11" //10.2.1
      val akkaV     = "2.5.26" //2.6.10"
      Seq(
        "org.flywaydb" % "flyway-core" % "5.0.6",
        "net.codingwell" %% "scala-guice" % "4.2.11",
        "joda-time" % "joda-time" % "2.10.8",
        "com.pauldijou" %% "jwt-play-json" % "4.2.0",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
        "com.typesafe.slick" %% "slick" % "3.3.2",
        "org.sangria-graphql" %% "sangria" % "1.4.2",
        "org.mockito" % "mockito-core" % "2.23.0", //% Test,
        "com.typesafe.akka" %% "akka-stream" % akkaV,
        "com.typesafe.akka" %% "akka-http" % akkaHttpV, //% Test,
        "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
        "com.typesafe.akka" %% "akka-stream-testkit" % akkaV, //% Test,
        "com.typesafe.akka" %% "akka-slf4j" % akkaV,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV, // % Test,
        "org.scalatest" %% "scalatest" % "3.0.5" % "compile, test",
        "mysql" % "mysql-connector-java" % "8.0.18",
        "com.github.pureconfig" %% "pureconfig" % "0.12.1",
        "com.google.inject" % "guice" % "4.2.3",
        "com.pauldijou" %% "jwt-core" % "4.2.0",
        "com.typesafe" % "config" % "1.3.4",
        "com.typesafe.akka" %% "akka-actor" % "2.5.26",
        "com.typesafe.akka" %% "akka-http-core" % "10.1.11",
        "io.spray" %% "spray-json" % "1.3.5",
        "javax.inject" % "javax.inject" % "1",
        "org.sangria-graphql" %% "sangria-marshalling-api" % "1.0.3",
        "org.slf4j" % "slf4j-api" % "1.7.25",
      )
    },
  )
  .settings(dockerSettings)
  .enablePlugins(PackPlugin)
  .settings(
    bootdb := TestHelper.runDevelopmentStack(streams.value.log),
    parallelExecution in Test := false,
    docker := docker.dependsOn(pack).value,
  )
