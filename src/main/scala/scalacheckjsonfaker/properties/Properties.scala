package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue}
import scalacheckjsonfaker.config.Config
import scalacheckjsonfaker.schema.Schema

class Properties(schema: Schema, config: Config) {

  val extractors: List[JsObject => Option[Gen[JsValue]]] =
    List(
      ArrayProperty.extract(this, config.arrayConfig),
      BooleanProperty.extract,
      NumberProperty.extract(config.numberConfig),
      ObjectProperty.extract(this),
      StringProperty.extract(config.stringConfig),
      RefProperty.extract(schema, this)
    )

  def extract(obj: JsObject): Option[Gen[JsValue]] =
    extractors.foldLeft(None: Option[Gen[JsValue]]) { case (acc, f) =>
      if(acc.isEmpty) f(obj)
      else acc
    }
}