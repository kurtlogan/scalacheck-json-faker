package scalacheckjsonfaker

import org.scalacheck.Gen
import play.api.libs.json.{JsObject, JsValue, Json}
import scalacheckjsonfaker.properties.Properties

import scala.io.Source

object Faker {

  def from(s: String): Gen[String] = {
    Json.parse(s) match {
      case o@JsObject(_) => from(o).map(_.toString())
      case _             => throw new Exception("unable to read json")
    }
  }

  def from(o: JsObject): Gen[JsValue] = {
    Properties.extract(o).getOrElse(throw new Exception("Unknown type"))
  }
}

object Main extends App {

  val schema = Source.fromResource("test.json").getLines().toList.mkString

  println(Faker.from(schema).sample.get)


}