package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsNumber, JsObject, JsString, JsValue}

object NumberProperty {

  def extract(obj: JsObject): Option[Gen[JsValue]] = {
    val max = obj.value.get("maximum").map(_.as[Long]).getOrElse(Long.MaxValue)
    val min = obj.value.get("minimum").map(_.as[Long]).getOrElse(Long.MinValue)

    if(obj.value.get("type").contains(JsString("number"))) {
      Some(Gen.chooseNum(min, max).map(n => JsNumber(n)))
    } else {
      None
    }
  }
}