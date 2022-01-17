package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue}
import scalacheckjsonfaker.config.NumberConfig
import scalacheckjsonfaker.schema.Schema

object NumberProperty {

  def extract(config: NumberConfig)(obj: JsObject): Option[Gen[JsValue]] = {

    if(obj.value.get("type").contains(JsString("number"))) {
      val min = obj.value.get("minimum").map(_.as[Long]).getOrElse(config.minimum)
      val max = obj.value.get("maximum").map(_.as[Long]).getOrElse(config.maximum)

      Some(Gen.chooseNum(min, max).map(n => JsNumber(n)))
    } else {
      None
    }
  }
}