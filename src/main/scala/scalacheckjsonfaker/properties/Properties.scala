package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue}
import scalacheckjsonfaker.schema.Schema

object Properties {

  val extactors: List[(Schema, JsObject) => Option[Gen[JsValue]]] =
    List(
      ArrayProperty.extract,
      BooleanProperty.extract,
      NumberProperty.extract,
      ObjectProperty.extract,
      StringProperty.extract,
      RefProperty.extract
    )

  def extract(schema: Schema, obj: JsObject): Option[Gen[JsValue]] =
    extactors.foldLeft(None: Option[Gen[JsValue]]) { case (acc, f) =>
      if(acc.isEmpty) f(schema, obj)
      else acc
    }
}