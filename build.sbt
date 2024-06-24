import Dependencies._

ThisBuild / scalaVersion := "2.13.10"
ThisBuild / organization := "io.github.dataunitylab"

lazy val hello = (project in file("."))
  .settings(
    name := "JSONoid Server",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      scalatra,
      scalatraJson,
      logback,
      jsonoid,
      json4s,

      jetty % "container",
      servletApi % "provided",

      scalatraTest % Test,
    ),
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
