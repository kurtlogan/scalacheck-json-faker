package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue}

object Properties {

  val extactors: List[JsObject => Option[Gen[JsValue]]] =
    List(
      ArrayProperty.extract,
      BooleanProperty.extract,
      NumberProperty.extract,
      ObjectProperty.extract,
      StringProperty.extract,
    )

  def extract(obj: JsObject): Option[Gen[JsValue]] =
    extactors.foldLeft(None: Option[Gen[JsValue]]) { case (acc, f) =>
      if(acc.isEmpty) f(obj)
      else acc
    }
}