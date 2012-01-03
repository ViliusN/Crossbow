name := "crossbow-core"

version := "0.3"

organization := "lt.norma"

scalaVersion := "2.9.1"

scalacOptions := Seq("-deprecation", "-unchecked")

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

libraryDependencies += "joda-time" % "joda-time" % "2.0"

libraryDependencies += "org.joda" % "joda-convert" % "1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

crossPaths := false
