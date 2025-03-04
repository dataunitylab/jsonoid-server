package controllers

import scala.collection.mutable.Map

import javax.inject._
import play.api.libs.json._
import play.api.mvc._

import io.github.dataunitylab.jsonoid.discovery.{DiscoverSchema, JsonoidParams}
import io.github.dataunitylab.jsonoid.discovery.schemas.{
  JsonSchema,
  PropertySets,
  ZeroSchema
}

import play.api.libs.{json => pjson}
import org.{json4s => j4s}

object Conversions {
  def toJson4s(json: play.api.libs.json.JsValue): org.json4s.JValue =
    json match {
      case pjson.JsString(str)    => j4s.JString(str)
      case pjson.JsNull           => j4s.JNull
      case pjson.JsBoolean(value) => j4s.JBool(value)
      case pjson.JsTrue           => j4s.JBool(true)
      case pjson.JsFalse          => j4s.JBool(false)
      case pjson.JsNumber(value)  => j4s.JDecimal(value)
      case pjson.JsArray(items)   => j4s.JArray(items.map(toJson4s(_)).toList)
      case pjson.JsObject(items) =>
        j4s.JObject(items.map { case (k, v) => k -> toJson4s(v) }.toList)
    }

  def toPlayJson(json: org.json4s.JValue): play.api.libs.json.JsValue =
    json match {
      case j4s.JString(str)    => pjson.JsString(str)
      case j4s.JNothing        => pjson.JsNull
      case j4s.JNull           => pjson.JsNull
      case j4s.JDecimal(value) => pjson.JsNumber(value)
      case j4s.JDouble(value)  => pjson.JsNumber(value)
      case j4s.JInt(value)     => pjson.JsNumber(BigDecimal(value))
      case j4s.JLong(value)    => pjson.JsNumber(BigDecimal(value))
      case j4s.JBool(value)    => pjson.JsBoolean(value)
      case j4s.JSet(fields)   => pjson.JsArray(fields.toList.map(toPlayJson(_)))
      case j4s.JArray(fields) => pjson.JsArray(fields.map(toPlayJson(_)))
      case j4s.JObject(fields) =>
        pjson.JsObject(fields.map { case (k, v) => k -> toPlayJson(v) }.toMap)
    }

  def toPlayJson(json: org.json4s.JObject): play.api.libs.json.JsObject =
    pjson.JsObject(json.obj.map { case (k, v) => k -> toPlayJson(v) }.toMap)
}

final case class SchemaParams(name: String, propSet: Option[String] = None)
object SchemaParams {
  implicit val schemaParamsRead: Reads[SchemaParams] = Json.reads[SchemaParams]
}
import SchemaParams._

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class JsonoidController @Inject() (val cc: ControllerComponents)
    extends AbstractController(cc) {
  private val schemas = Map.empty[String, JsonSchema[_]]
  private val jsonoidParams = Map.empty[String, JsonoidParams]

  def status() = Action { implicit request: Request[AnyContent] =>
    Ok(Json.obj("status" -> "ok"))
  }

  def postSchema() = Action(parse.json) { implicit request =>
    val schemaParamsResult = request.body.validate[SchemaParams]
    schemaParamsResult.fold(
      errors => {
        BadRequest(
          Json.obj("status" -> "error", "error" -> JsError.toJson(errors))
        )
      },
      schemaParams => {
        if (schemas.contains(schemaParams.name)) {
          Conflict(Json.obj("error" -> "Schema already exists"))
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
              Created(Json.obj("status" -> "ok"))
            case None =>
              BadRequest(Json.obj("error" -> "Invalid property set"))
          }
        }
      }
    )
  }

  def deleteSchema(name: String) = Action { implicit request =>
    if (schemas.contains(name)) {
      schemas.remove(name)
      Ok(Json.obj("status" -> "ok"))
    } else {
      NotFound(Json.obj("error" -> "Schema not found"))
    }
  }

  def getSchema(name: String) = Action { implicit request =>
    schemas.get(name) match {
      case Some(schema) =>
        Ok(Json.obj("schema" -> Conversions.toPlayJson(schema.toJson())))
      case None => NotFound(Json.obj("error" -> "Schema not found"))
    }
  }

  def putSchema(name: String) = Action(parse.json) { implicit request =>
    schemas.get(name) match {
      case Some(schema) =>
        assert(jsonoidParams.contains(name))
        val p = jsonoidParams.get(name).get
        val newSchema = DiscoverSchema.discoverFromValue(
          Conversions.toJson4s(request.body)
        )(p)
        newSchema match {
          case Some(newSchema) =>
            schemas.put(name, schema.merge(newSchema)(p))
            Ok(Json.obj("status" -> "ok"))
          case None =>
            BadRequest(Json.obj("error" -> "Invalid schema"))
        }
      case None => NotFound(Json.obj("error" -> "Schema not found"))
    }
  }
}
