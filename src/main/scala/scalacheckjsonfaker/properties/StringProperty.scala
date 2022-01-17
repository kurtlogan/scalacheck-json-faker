package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsString, JsValue}
import scalacheckjsonfaker.config.StringConfig
import scalacheckjsonfaker.schema.Schema
import wolfendale.scalacheck.regexp.RegexpGen

object StringProperty {

  val stringType = "string"

  def extract(config: StringConfig)(obj: JsObject): Option[Gen[JsValue]] = {
    if(obj.value.get("type").contains(JsString("string"))) {

      val min = obj.value.get("minLength").map(_.as[Int]).getOrElse(config.minLength)
      val max = obj.value.get("maxLength").map(_.as[Int]).getOrElse(config.maxLength)
      val enum = obj.value.get("enum").map(_.as[List[String]])
      val pattern = obj.value.get("pattern").map(_.as[String])

      val pattenGen = pattern.map(p => RegexpGen.from(p))
      val enumGen = enum.map(xs => Gen.oneOf(xs))
      val minMaxGen = Gen.chooseNum(min, max).flatMap { n =>
        Gen.listOfN(n, Gen.alphaNumChar).map(_.mkString)
      }

      Some(pattenGen.orElse(enumGen).getOrElse(minMaxGen).map(JsString))
    } else {
      None
    }
  }
}