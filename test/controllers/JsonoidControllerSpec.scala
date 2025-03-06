package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._


class JsonoidControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "JsonoidController /status" should {
    "return ok" in {
      val controller = app.injector.instanceOf[JsonoidController]
      val result = controller.status().apply(FakeRequest())
      status(result) mustBe OK
    }
  }
}
