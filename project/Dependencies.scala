import sbt._

object Dependencies {
  // Runtime
  lazy val jacksonModule = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.17.1"
  lazy val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % "2.17.1"
  lazy val jsonoid = "io.github.dataunitylab" %% "jsonoid-discovery"  % "0.30.1"

  // Test
  lazy val testPlusPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1"
}
