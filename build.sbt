ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.5"

val circeVersion = "0.14.1"

lazy val root = (project in file(".")).settings(
  name := "kafka-performance-test",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.1.1",
    "org.typelevel" %% "cats-effect-kernel" % "3.1.1",
    "org.typelevel" %% "cats-effect-std" % "3.1.1",
    "com.github.fd4s" %% "fs2-kafka" % "2.1.0",
    "org.typelevel"              %% "log4cats-slf4j"           % "2.1.1",
    "org.scalacheck" %% "scalacheck" % "1.15.4",
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    ),
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    ).map(_ % circeVersion)
  )
