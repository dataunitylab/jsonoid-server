import Dependencies._

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / versionScheme := Some("early-semver")
ThisBuild / organization := "io.github.dataunitylab"
ThisBuild / organizationName := "Rochester Institute of Technology"

inThisBuild(
  List(
    organization := "io.github.dataunitylab",
    homepage := Some(url("https://github.com/dataunitylab/jsonoid-server")),
    licenses := List("MIT" -> url("http://opensource.org/licenses/MIT")),
    developers := List(
      Developer(
        "michaelmior",
        "Michael Mior",
        "mmior@mail.rit.edu ",
        url("https://michael.mior.ca")
      )
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )
)

scalafixOnCompile := true
ThisBuild / scalafixDependencies += "net.pixiv" %% "scalafix-pixiv-rule" % "4.5.3"

Global / onChangedBuildSource := ReloadOnSourceChanges

val nonConsoleCompilerOptions = Seq(
  "-feature",
  "-Xfatal-warnings",
  "-Ywarn-unused:imports",
  "-Wconf:cat=unused-imports&site=<empty>:s", // Silence import warnings on Play `routes` files
  "-Wconf:cat=unused-imports&site=router:s", // Silence import warnings on Play `routes` files
  "-Wconf:cat=unused-imports&site=v1:s", // Silence import warnings on Play `v1.routes` files
  "-Wconf:cat=unused-imports&site=v2:s", // Silence import warnings on Play `v2.routes` files
  "-deprecation",
  "-release:8"
)

lazy val root = (project in file("."))
  .settings(
    name := "jsonoid-server",
    libraryDependencies ++= Seq(
      guice,
      jacksonModule,
      jacksonDatabind,
      jsonoid,
      testPlusPlay % Test,
    ),
    javacOptions ++= Seq("-source", "11", "-target", "11"),
    scalacOptions ++= nonConsoleCompilerOptions,
    buildInfoKeys := Seq[BuildInfoKey](version),
    buildInfoPackage := "io.github.dataunitylab.jsonoid.server"
  )

enablePlugins(PlayScala)
