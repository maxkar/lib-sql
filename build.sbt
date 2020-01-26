name := "lib-sql"

organization := "ru.maxkar"

description := 
  """The library provides Scala API over the standard JDBC layer. It focus
    | on everyday tasks like writing queries and parsing results for
    | applications having a fixed database schema.""".stripMargin

version := "0.0.1-SNAPSHOT"

scalaSource in Compile := baseDirectory.value / "src"

crossScalaVersions := Seq("2.11.12", "2.12.10", "2.13.1")

scalaVersion := "2.13.1"

