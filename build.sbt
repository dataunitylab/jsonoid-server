val ScalatraVersion = "2.7.1"

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "io.github.dataunitylab"

lazy val hello = (project in file("."))
  .settings(
    name := "JSONoid Server",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.43.v20210629" % "container",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
      "io.github.dataunitylab" %% "jsonoid-discovery" % "0.20.1",
      "org.scalatra" %% "scalatra-json" % ScalatraVersion,
      "org.json4s"   %% "json4s-jackson" % "3.7.0-M11",
    ),
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
