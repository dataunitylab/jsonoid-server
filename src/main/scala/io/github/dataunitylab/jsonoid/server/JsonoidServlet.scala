package io.github.dataunitylab.jsonoid.server

import scala.collection.mutable.Map

import io.github.dataunitylab.jsonoid.discovery.{DiscoverSchema, JsonoidParams}
import io.github.dataunitylab.jsonoid.discovery.schemas.{
  JsonSchema,
  PropertySets,
  ZeroSchema
}
import org.json4s.{DefaultFormats, Formats}
import org.scalatra._
import org.scalatra.json._

final case class SchemaParams(name: String, propSet: Option[String] = None)

class JsonoidServlet extends ScalatraServlet with JacksonJsonSupport {
  protected implicit val jsonFormats: Formats = DefaultFormats
  private val schemas = Map.empty[String, JsonSchema[_]]
  private val jsonoidParams = Map.empty[String, JsonoidParams]

  before() {
    contentType = formats("json")
  }

  post("/schemas") {
    val schemaParams = parsedBody.extract[SchemaParams]
    if (schemas.contains(schemaParams.name)) {
      Conflict("error" -> "Schema already exists")
    } else {
      val maybePropSet = schemaParams.propSet match {
        case Some("All") =>
          Some(PropertySets.AllProperties)
        case Some("Simple") =>
          Some(PropertySets.SimpleProperties)
        case Some("Min") =>
          Some(PropertySets.MinProperties)
        case Some(_) =>
          None
        case None =>
          Some(PropertySets.AllProperties)
      }

      maybePropSet match {
        case Some(propSet) =>
          schemas.put(schemaParams.name, ZeroSchema())
          jsonoidParams.put(
            schemaParams.name,
            JsonoidParams().withPropertySet(propSet)
          )
          Created("status" -> "ok")
        case None =>
          BadRequest("error" -> "Invalid property set")
      }
    }
  }

  get("/schemas/:name") {
    val name = params("name")
    schemas.get(name) match {
      case Some(schema) => Ok(schema.toJson())
      case None         => NotFound("error" -> "Schema not found")
    }
  }

  put("/schemas/:name") {
    val name = params("name")
    schemas.get(name) match {
      case Some(schema) =>
        assert(jsonoidParams.contains(name))
        @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
        val p = jsonoidParams.get(name).get

        val newSchema = DiscoverSchema.discoverFromValue(parsedBody)(p)
        newSchema match {
          case Some(newSchema) =>
            schemas.put(name, schema.merge(newSchema)(p))
            Ok("status" -> "ok")
          case None =>
            BadRequest("error" -> "Invalid schema")
        }
      case None =>
        NotFound("error" -> "Schema not found")
    }
  }

}
