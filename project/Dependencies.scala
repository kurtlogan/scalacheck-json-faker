import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5"
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.1"
  lazy val playJson = "com.typesafe.play" %% "play-json" % "2.8.2"
}
