package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}

object ObjectProperty {

  def extract(properties: Properties)(obj: JsObject): Option[Gen[JsValue]] = {

    if(obj.value.get("type").contains(JsString("object"))) {
      val required = obj.value.get("required").collect {
        case JsArray(value) => value.collect {
          case JsString(value) => value
          case _               => throw new Exception("required field name not of type string")
        }
        case _              => throw new Exception("required not type of array")
      }.getOrElse(List())

      val props = obj.value.get("properties").collect {
        case JsObject(underlying) => underlying
        case _                    => throw new Exception("properties not of type object")
      }.get

      require(props.keys.toSet.intersect(required.toSet) == required.toSet, "not all required fields found in properties")

      val propGen = props.keys.map { k =>
        val obj = props.get(k).collect {
          case obj@JsObject(_) => obj
          case _               => throw new Exception("object property must be an object")
        }.get

        properties.extract(obj).getOrElse(throw new Exception("unknown data type"))
          .flatMap { js =>
            val objWithKey = JsObject(Map(k -> js))
            Gen.option(js).map(_.fold(objWithKey)(_ => objWithKey))
          }
      }

      val mapGen =
        Gen.sequence[List[JsValue], JsValue](propGen).map { a =>
          a.filter(_.isInstanceOf[JsObject]).map(_.asInstanceOf[JsObject])
            .foldLeft(JsObject.empty)((a: JsObject, b: JsObject) => a ++ b)
        }

      Some(mapGen)
    } else {
      None
    }
  }
}