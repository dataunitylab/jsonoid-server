name := """jsonoid-server"""
organization := "io.github.dataunitylab.jsonoid"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "io.github.dataunitylab" %% "jsonoid-discovery"  % "0.30.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.1"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "io.github.dataunitylab.jsonoid.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "io.github.dataunitylab.jsonoid.binders._"
