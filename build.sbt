import sbt.Keys._

val AkkaVersion = "2.6.19"

lazy val root = (project in file("."))
  .enablePlugins(PlayService, PlayLayoutPlugin, JavaAppPackaging)
  .settings(
    name := "scala-web-crawler",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      guice,
      cacheApi,
      "org.joda" % "joda-convert" % "2.2.1",
      "net.logstash.logback" % "logstash-logback-encoder" % "6.2",
      "io.lemonlabs" %% "scala-uri" % "1.5.1",
      "com.ning" % "async-http-client" % "1.9.40",
      "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
      "net.ruippeixotog" %% "scala-scraper" % "2.2.1",
      "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-protobuf-v3" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion % Test,
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
      "org.scalatest" %% "scalatest" % "3.2.10" % Test,
      "org.scalatestplus" %% "mockito-3-4" % "3.2.1.0" % Test
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings"
    )
  )

