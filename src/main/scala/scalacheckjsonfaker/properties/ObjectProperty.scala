package scalacheckjsonfaker.properties

import org.scalacheck.Gen
import play.api.libs.json.{JsArray, JsObject, JsString, JsValue}
import scalacheckjsonfaker.schema.Schema

object ObjectProperty {

  def extract(schema: Schema, obj: JsObject): Option[Gen[JsValue]] = {

    if(obj.value.get("type").contains(JsString("object"))) {
      val required = obj.value.get("required").collect {
        case JsArray(value) => value.collect {
          case JsString(value) => value
          case _               => throw new Exception("required field name not of type string")
        }
        case _              => throw new Exception("required not type of array")
      }.getOrElse(List())

      val properties = obj.value.get("properties").collect {
        case JsObject(underlying) => underlying
        case _                    => throw new Exception("properties not of type object")
      }.get

      require(properties.keys.toSet.intersect(required.toSet) == required.toSet, "not all required fields found in properties")

      val propGen = properties.keys.map { k =>
        val obj = properties.get(k).collect {
          case obj@JsObject(_) => obj
          case _               => throw new Exception("object property must be an object")
        }.get

        Properties.extract(schema, obj).getOrElse(throw new Exception("unknown data type"))
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