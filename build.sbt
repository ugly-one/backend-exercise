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
    deployConfigurationName := name.value,
    libraryDependencies ++= {
      val commonsV  = "8.0.2"
      val akkaHttpV = "10.1.11"
      val akkaV     = "2.5.26"
      Seq(
        "com.leoilab" %% "platform-commons-id-slick" % commonsV,
        "com.leoilab" %% "platform-commons-audit" % commonsV,
        "com.leoilab" %% "platform-commons-db" % commonsV,
        "com.leoilab" %% "platform-commons-config" % commonsV,
        "com.leoilab" %% "platform-commons-akka" % commonsV,
        "com.leoilab" %% "platform-commons-health" % commonsV,
        "com.leoilab" %% "platform-commons-sensible-rest" % commonsV,
        "com.leoilab" %% "platform-commons-sensible-rest-client" % commonsV % "test",
        "org.sangria-graphql" %% "sangria" % "1.4.2",
        "com.leoilab" %% "sangria-spray-json" % "1.0.3-LEO",
        "org.mockito" % "mockito-core" % "2.23.0" % Test,
        "com.typesafe.akka" %% "akka-http" % akkaHttpV % Test,
        "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
        "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
        "com.typesafe.akka" %% "akka-slf4j" % akkaV,
        "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test,
        "io.github.erdeszt" %% "graphient" % "0.1.15",
        "org.scalatest" %% "scalatest" % "3.0.5" % "compile, test",
        "mysql" % "mysql-connector-java" % "8.0.18",
        "com.github.pureconfig" %% "pureconfig" % "0.12.1",
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
