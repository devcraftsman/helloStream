import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "HelloStream",

    resolvers += "Akka maven latest" at "https://dl.bintray.com/akka/maven/",

    // dependecies
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % "2.5.6"

    ),

    // alpakka
    libraryDependencies ++= Seq(
      "com.lightbend.akka" %% "akka-stream-alpakka-ftp" % "0.14"
    ),

    // test dependecies
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % Test,
      "com.typesafe.akka" %%"akka-stream-testkit" % "2.5.6" % Test,
      scalaTest % Test
    )
  )
