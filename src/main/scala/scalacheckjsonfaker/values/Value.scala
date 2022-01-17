package scalacheckjsonfaker.values

import play.api.libs.json.{JsObject, Reads}

sealed trait Value[T] {

  val obj: JsObject
  val key: String

  def asOpt(implicit ev: Reads[T]): Option[T] = obj.value.get(key).flatMap(_.asOpt[T])

  def getOrDefault(default: T)(implicit ev: Reads[T]): T = asOpt.getOrElse(default)
}

case class Minimum(obj: JsObject) extends Value[Long] {
  val key = "minimum"
}

case class Maximum(obj: JsObject) extends Value[Long] {
  val key = "maximum"
}

case class MinLength(obj: JsObject) extends Value[Int] {
  val key = "minLength"
}

case class MaxLength(obj: JsObject) extends Value[Int] {
  val key = "maxLength"
}

case class Enum(obj: JsObject) extends Value[Seq[String]] {
  val key = "enum"
}

case class Pattern(obj: JsObject) extends Value[String] {
  val key = "pattern"
}

case class MinItems(obj: JsObject) extends Value[Int] {
  val key = "minItems"
}

case class MaxItems(obj: JsObject) extends Value[Int] {
  val key = "maxItems"
}

case class Items(obj: JsObject) extends Value[JsObject] {
  val key = "items"
}

case class Ref(obj: JsObject) extends Value[String] {
  val key = "$ref"
}

case class Type(obj: JsObject) extends Value[String] {

  val key = "type"

  def contains(s: String): Boolean = asOpt.contains(s)
}

case class Required(obj: JsObject) extends Value[Seq[String]] {
  val key = "required"
}

case class Properties(obj: JsObject) extends Value[JsObject] {
  val key = "properties"
}