package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsString, JsValue}
import scalacheckjsonfaker.schema.Schema
import wolfendale.scalacheck.regexp.RegexpGen

object StringProperty {

  val stringType = "string"

  def extract(schema: Schema, obj: JsObject): Option[Gen[JsValue]] = {
    if(obj.value.get("type").contains(JsString("string"))) {
      val min = obj.value.get("minLength").map(_.as[Int]).getOrElse(0)
      val max = obj.value.get("maxLength").map(_.as[Int]).getOrElse(2000)
      val enum = obj.value.get("enum").map(_.as[List[String]])
      val pattern = obj.value.get("pattern").map(_.as[String])

      val gen = pattern.map(p => RegexpGen.from(p)).getOrElse(enum.map(xs => Gen.oneOf(xs)).getOrElse {
        Gen.chooseNum(min, max).flatMap { n =>
          Gen.listOfN(n, Gen.alphaNumChar).map(_.mkString)
        }
      })

      Some(gen.map(JsString))
    } else {
      None
    }
  }
}