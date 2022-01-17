package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsBoolean, JsObject, JsString, JsValue}
import scalacheckjsonfaker.schema.Schema

object BooleanProperty {

  def extract(obj: JsObject): Option[Gen[JsValue]] = {
    if(obj.value.get("type").contains(JsString("boolean"))) {
      Some(Gen.oneOf(true, false).map(JsBoolean))
    } else {
      None
    }
  }
}