name := "ejemplo_mongo"

version := "1.0"

scalaVersion := "2.11.7"

mainClass in (Compile,run) := Some("Main")

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.2.7",
  "org.scala-sbt" % "io" % "0.13.9",
  "org.mongodb" %% "casbah" % "2.8.2",
  "joda-time" % "joda-time" % "2.8.2",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "mysql" % "mysql-connector-java" % "5.1.25"
)