package scalacheckjsonfaker

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue, Json}
import scalacheckjsonfaker.properties.Properties
import scalacheckjsonfaker.schema.Schema

object JsonGen {

  def from(s: String): Gen[String] = {
    Json.parse(s) match {
      case o@JsObject(_) => from(o).map(_.toString())
      case _             => throw new Exception("unable to read json")
    }
  }

  def from(o: JsObject): Gen[JsValue] = {
    Properties.extract(Schema(o), o).getOrElse(throw new Exception("Unknown type"))
  }
}