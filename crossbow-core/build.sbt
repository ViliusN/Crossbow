name := "crossbow-core"

version := "0.1"

organization := "lt.norma"

scalaVersion := "2.9.0-1"

libraryDependencies += "org.scalatest" % "scalatest_2.9.0-1" % "1.6.1"

libraryDependencies += "joda-time" % "joda-time" % "1.6.2"

scalaSource in Compile <<= baseDirectory(_ / "src")

scalaSource in Test <<= baseDirectory(_ / "test")

target <<= baseDirectory(_ / "bin")

