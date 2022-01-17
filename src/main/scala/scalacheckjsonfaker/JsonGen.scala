package scalacheckjsonfaker

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue, Json}
import scalacheckjsonfaker.config.Config
import scalacheckjsonfaker.properties.Properties
import scalacheckjsonfaker.schema.Schema

object JsonGen {

  private val defaultConfig = Config.default

  def from(s: String): Gen[String] = from(s, defaultConfig)

  def from(s: String, config: Config): Gen[String] = {
    Json.parse(s) match {
      case o@JsObject(_) => from(o, config).map(_.toString())
      case _             => throw new Exception("unable to read json")
    }
  }

  def from(o: JsObject): Gen[JsValue] = from(o, defaultConfig)

  def from(o: JsObject, config: Config): Gen[JsValue] = {
    val properties = new Properties(Schema(o), config)
    properties.extract(o).getOrElse(throw new Exception("Unknown type"))
  }
}