package io.github.dataunitylab.jsonoid.server

import org.scalatra.test.scalatest._

class JsonoidServletTests extends ScalatraFunSuite {

  addServlet(classOf[JsonoidServlet], "/*")

  test("GET /schemas/foo on JsonoidServlet should return status 404") {
    get("/schemas/foo") {
      status should equal(404)
    }
  }

  test("POST /schemas on JsonoidServlet should return status 200") {
    post("/schemas", """{"name": "test", "propSet": "All"}""") {
      status should equal(201)
    }
  }

}
