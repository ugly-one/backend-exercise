resolvers ++= Seq(
  "Leo ilab maven snapshots" at "s3://s3-eu-west-1.amazonaws.com/leo-platform-packages/snapshots",
  "Leo ilab maven releases" at "s3://s3-eu-west-1.amazonaws.com/leo-platform-packages/releases",
)

addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.5.0")

addSbtPlugin("com.leoilab" % "sbt-platform-services-infrastructure" % "4.3.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

//addSbtPlugin("com.leoilab" % "sbt-platform-canpublish" % "0.5.0")

addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.10.0")

//addSbtPlugin("com.leoilab" % "sbt-git-stamp" % "1.0.0")

//addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("com.github.cb372" % "sbt-explicit-dependencies" % "0.2.9")

addSbtPlugin("com.leoilab" % "scala-build-config-sbt-plugin" % "2.9")
