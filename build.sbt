import Dependencies._

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "HelloStream",
    Defaults.itSettings,

    resolvers += Resolver.bintrayRepo("akka", "maven"),

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
      "com.typesafe.akka" %% "akka-testkit" % "2.5.6" % "it,test",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.6" % "it,test",
      scalaTest % "it,test"
    )
  )
