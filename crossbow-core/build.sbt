name := "crossbow-core"

version := "0.2"

organization := "lt.norma"

scalaVersion := "2.9.1"

scalacOptions := Seq("-deprecation", "-unchecked")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

libraryDependencies += "org.scalatest" % "scalatest_2.9.1" % "1.6.1"

libraryDependencies += "joda-time" % "joda-time" % "2.0"

libraryDependencies += "org.joda" % "joda-convert" % "1.1"
