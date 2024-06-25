import Dependencies._

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / versionScheme    := Some("early-semver")
ThisBuild / organization     := "io.github.dataunitylab"
ThisBuild / organizationName := "Rochester Institute of Technology"

val nonConsoleCompilerOptions = Seq(
  "-feature",
  "-Xfatal-warnings",
  "-Ywarn-unused:imports",
  "-deprecation",
  "-release:8"
)

lazy val root = (project in file("."))
  .settings(
    name := "JSONoid Server",
    version := "0.1.0-SNAPSHOT",
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

    scalacOptions ++= nonConsoleCompilerOptions,
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision
  )

Compile / compile / wartremoverErrors ++= Seq(
  Wart.ArrayEquals,
  Wart.EitherProjectionPartial,
  Wart.Enumeration,
  Wart.Equals,
  Wart.ExplicitImplicitTypes,
  Wart.FinalCaseClass,
  Wart.FinalVal,
  Wart.JavaConversions,
  Wart.JavaSerializable,
  Wart.LeakingSealed,
  Wart.Null,
  Wart.Option2Iterable,
  Wart.OptionPartial,
  Wart.Product,
  Wart.PublicInference,
  Wart.Recursion,
  Wart.Return,
  Wart.Serializable,
  Wart.StringPlusAny,
  Wart.ToString,
  Wart.TripleQuestionMark,
  Wart.TryPartial,
  Wart.Var,
  Wart.While,
)


scalafixOnCompile := true
ThisBuild / scalafixDependencies += "net.pixiv" %% "scalafix-pixiv-rule" % "3.0.1"

Global / onChangedBuildSource := ReloadOnSourceChanges

enablePlugins(DockerPlugin)
enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)

git.remoteRepo := "git@github.com:dataunitylab/jsonoid-server.git"
git.useGitDescribe := true

ThisBuild / dynverSonatypeSnapshots := true
ThisBuild / dynverSeparator := "-"

docker / dockerfile := {
  // The assembly task generates a fat JAR file
  val artifact: File = sbt.Keys.`package`.value
  val artifactTargetPath = s"/var/lib/jetty/webapps/ROOT.war"

  new Dockerfile {
    from("jetty:9.4-jre8-alpine")
    add(artifact, artifactTargetPath)
    expose(8080)
  }
}
