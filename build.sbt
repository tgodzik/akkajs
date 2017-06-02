name := """akkajs2"""

version := "1.0-SNAPSHOT"

resolvers += "Sonatype" at "https://oss.sonatype.org/content/repositories/releases/"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-jackson" % "3.2.11",
  "org.webjars" % "angularjs" % "1.3.0-beta.2",
  "org.mongodb" %% "casbah" % "3.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.2",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
) 
