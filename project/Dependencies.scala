import sbt._


object Dependencies {
  val ScalatraVersion = "2.7.1"

  //Runtime
  lazy val scalatra     = "org.scalatra"           %% "scalatra"           % ScalatraVersion
  lazy val scalatraJson = "org.scalatra"           %% "scalatra-json"      % ScalatraVersion
  lazy val logback      = "ch.qos.logback"          % "logback-classic"    % "1.2.3" % "runtime"
  lazy val jetty        = "org.eclipse.jetty"       % "jetty-webapp"       % "9.4.43.v20210629"
  lazy val servletApi   = "javax.servlet"           % "javax.servlet-api"  % "3.1.0"
  lazy val jsonoid      = "io.github.dataunitylab" %% "jsonoid-discovery"  % "0.20.1"
  lazy val json4s       = "org.json4s"             %% "json4s-jackson"     % "3.7.0-M11"
 
  // Test
  lazy val scalatraTest = "org.scalatra"           %% "scalatra-scalatest" % ScalatraVersion
}
