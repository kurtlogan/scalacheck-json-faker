package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}

object ArrayProperty {

  def extract(obj: JsObject): Option[Gen[JsValue]] = {

    if(obj.value.get("type").contains(JsString("array"))) {
      val items = obj.value.getOrElse("items", throw new Exception("Items is required"))
      val itemGen = Properties.extract(items.asInstanceOf[JsObject]).getOrElse(throw new Exception("Unknown object type"))

      val min = obj.value.get("minItems").map(_.as[Int]).getOrElse(0)
      val max = obj.value.get("maxItems").map(_.as[Int]).getOrElse(100)

      Some(Gen.chooseNum(min, max).flatMap(n => Gen.listOfN(n, itemGen)).map(xs => JsArray(xs)))
    } else {
      None
    }
  }
}