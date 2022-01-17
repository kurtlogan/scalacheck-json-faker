package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue}
import scalacheckjsonfaker.schema.Schema


object RefProperty {

  def extract(schema: Schema, properties: Properties)(obj: JsObject): Option[Gen[JsValue]] = {

    for {
      ref  <- obj.value.get("$ref")
      o    <- schema.find(ref.as[String])
      next <- properties.extract(o)
    } yield next
  }
}