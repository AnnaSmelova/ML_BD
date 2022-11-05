ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "MLBD_hw3_LR"
  )

libraryDependencies ++= Seq(
  "org.scalanlp" %% "breeze" % "2.1.0",
  "ch.qos.logback" % "logback-classic" % "1.4.4",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
)