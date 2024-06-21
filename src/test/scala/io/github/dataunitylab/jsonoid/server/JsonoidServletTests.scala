package io.github.dataunitylab.jsonoid.server

import org.scalatra.test.scalatest._

class JsonoidServletTests extends ScalatraFunSuite {

  addServlet(classOf[JsonoidServlet], "/*")

  test("GET / on JsonoidServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
