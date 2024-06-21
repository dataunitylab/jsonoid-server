package io.github.dataunitylab.jsonoid.server

import scala.collection.mutable.Map

import io.github.dataunitylab.jsonoid.discovery.DiscoverSchema
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

  get("/schemas/:name") {
    val name = params("name")
    schemas.get(name) match {
      case Some(schema) => Ok(schema.toJson)
      case None => NotFound("error" -> "Schema not found")
    }
  }

  put("/schemas/:name") {
    val name = params("name")
    schemas.get(name) match {
      case Some(schema) =>
        val newSchema = DiscoverSchema.discoverFromValue(parsedBody)
        newSchema match {
          case Some(newSchema) =>
            schemas.put(name, schema.merge(newSchema))
            Ok("status" -> "ok")
          case None =>
            BadRequest("error" -> "Invalid schema")
        }
      case None =>
        NotFound("error" -> "Schema not found")
    }
  }

}
