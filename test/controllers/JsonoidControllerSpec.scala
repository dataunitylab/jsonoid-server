package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

class JsonoidControllerSpec
    extends PlaySpec
    with GuiceOneAppPerTest
    with Injecting {
  "JsonoidController /status" should {
    "return ok" in {
      val controller = app.injector.instanceOf[JsonoidController]
      val result = controller.status().apply(FakeRequest())
      status(result) mustBe OK
    }
  }

  "JsonoidController POST /schemas" should {
    "return ok" in {
      val body = Json.obj("name" -> "foo")
      val result =
        route(app, FakeRequest(POST, "/schemas").withJsonBody(body)).get
      status(result) mustBe CREATED
    }

    "return an error if the schema already exists" in {
      val body = Json.obj("name" -> "foo")
      val request = FakeRequest(POST, "/schemas").withJsonBody(body)
      status(route(app, request).get) mustBe CREATED
      val result = route(app, request).get
      status(result) mustBe CONFLICT
    }
  }

  "JsonoidController DELETE /schemas/foo" should {
    "return not found when schema does not exist" in {
      val result = route(app, FakeRequest(DELETE, "/schemas/foo")).get
      status(result) mustBe NOT_FOUND
    }

    "successfully delete a schema" in {
      status(
        route(
          app,
          FakeRequest(POST, "/schemas").withJsonBody(Json.obj("name" -> "foo"))
        ).get
      ) mustBe CREATED
      val result = route(app, FakeRequest(DELETE, "/schemas/foo")).get
      status(result) mustBe OK
    }
  }

  "JsonoidController PUT /schemas/foo" should {
    val obj = Json.obj("foo" -> "bar")

    "return not found when schema does not exist" in {
      val result =
        route(app, FakeRequest(PUT, "/schemas/foo").withJsonBody(obj)).get
      status(result) mustBe NOT_FOUND
    }

    "successfully delete a schema" in {
      status(
        route(
          app,
          FakeRequest(POST, "/schemas").withJsonBody(Json.obj("name" -> "foo"))
        ).get
      ) mustBe CREATED
      var result =
        route(app, FakeRequest(PUT, "/schemas/foo").withJsonBody(obj)).get
      status(result) mustBe OK

      result = route(app, FakeRequest(GET, "/schemas/foo")).get
      (contentAsJson(result) \ "schema" \ "type").as[String] mustBe "object"
    }
  }
}
