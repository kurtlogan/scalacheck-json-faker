package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsString, JsValue}
import wolfendale.scalacheck.regexp.RegexpGen

object StringProperty {

  val stringType = "string"

  def extract(obj: JsObject): Option[Gen[JsValue]] = {
    if(obj.value.get("type").contains(JsString("string"))) {
      val min = obj.value.get("minLength").map(_.as[Int]).getOrElse(0)
      val max = obj.value.get("maxLength").map(_.as[Int]).getOrElse(Int.MaxValue)
      val enum = obj.value.get("enum").map(_.as[List[String]])
      val pattern = obj.value.get("pattern").map(_.as[String])

      val gen = pattern.map(p => RegexpGen.from(p)).getOrElse(enum.map(xs => Gen.oneOf(xs)) getOrElse {
        Gen.alphaNumStr
          .map(_.take(max))
          .suchThat(_.length >= min)
          .suchThat(_.length <= max)
      })

      Some(gen.map(JsString))
    } else {
      None
    }
  }
}