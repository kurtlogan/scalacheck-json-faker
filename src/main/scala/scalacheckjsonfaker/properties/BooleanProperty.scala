package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsBoolean, JsObject, JsValue}
import scalacheckjsonfaker.values.Type

object BooleanProperty {

  def gen(obj: JsObject): Option[Gen[JsValue]] = {
    if(Type(obj).is("boolean")) {
      Some(Gen.oneOf(true, false).map(JsBoolean))
    } else {
      None
    }
  }
}