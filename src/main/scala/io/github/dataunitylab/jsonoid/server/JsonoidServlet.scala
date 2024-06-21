package io.github.dataunitylab.jsonoid.server

import scala.collection.mutable.Map

import io.github.dataunitylab.jsonoid.discovery.schemas.{JsonSchema, ZeroSchema}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._


case class SchemaName(name: String)

class JsonoidServlet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats
  val schemas = Map.empty[String, JsonSchema[_]]

  before() {
    contentType = formats("json")
  }

  post("/schemas") {
    val name = parsedBody.extract[SchemaName].name
    if (schemas.contains(name)) {
      Conflict("error" -> "Schema already exists")
    } else {
      schemas.put(name, ZeroSchema())
      Created("status" -> "ok")
    }
  }

}
