import Dependencies._


lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    inThisBuild(List(
      organization := "com.example",
      name := "HelloStream",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT",
    )),

    // flyway
    flywayUrl := "jdbc:oracle:thin:@localhost:11521:XE",
    flywayUser := "TEST_USER",
    flywayPassword := "TEST_USER",
    flywaySchemas := Seq("TEST"),
    flywayLocations := Seq("filesystem:src/db/migrations"),


    Defaults.itSettings,
    resolvers += Resolver.bintrayRepo("akka", "maven"),
    resolvers += Resolver.mavenLocal,

    // dependecies
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-stream" % "2.5.6",
      "com.oracle.jdbc" % "ojdbc6" % "11.2.0.4"
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
