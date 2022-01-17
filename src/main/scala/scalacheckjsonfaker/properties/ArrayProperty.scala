package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}
import scalacheckjsonfaker.config.ArrayConfig

object ArrayProperty {

  def extract(properties: Properties, config: ArrayConfig)(obj: JsObject): Option[Gen[JsValue]] = {

    if(obj.value.get("type").contains(JsString("array"))) {
      val items = obj.value.getOrElse("items", throw new Exception("Items is required"))
      val itemGen = properties.extract(items.asInstanceOf[JsObject]).getOrElse(throw new Exception("Unknown object type"))

      val min = obj.value.get("minItems").map(_.as[Int]).getOrElse(config.minItems)
      val max = obj.value.get("maxItems").map(_.as[Int]).getOrElse(config.maxItems)

      Some(Gen.chooseNum(min, max).flatMap(n => Gen.listOfN(n, itemGen)).map(xs => JsArray(xs)))
    } else {
      None
    }
  }
}