ThisBuild / organization := "com.example"
ThisBuild / scalaVersion := "2.13.5"

val circeVersion = "0.14.1"
val refinedV             = "0.9.26"
val newtypeV             = "0.4.4"
val kindProjectorV       = "0.13.0"

lazy val root = (project in file(".")).settings(
  name := "kafka-performance-test",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-effect" % "3.1.1",
    "org.typelevel" %% "cats-effect-kernel" % "3.1.1",
    "org.typelevel" %% "cats-effect-std" % "3.1.1",
    "com.github.fd4s" %% "fs2-kafka" % "2.1.0",
    "org.typelevel"              %% "log4cats-slf4j"           % "2.1.1",
    "org.scalacheck" %% "scalacheck" % "1.15.4",
    "io.estatico"                %% "newtype"                  % newtypeV,
    "eu.timepit"                 %% "refined"                  % refinedV,
    "eu.timepit"                 %% "refined-cats"             % refinedV,
    // better monadic for compiler plugin as suggested by documentation
    compilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1")
    ),
  scalacOptions += "-Ymacro-annotations", // need this for newtype library stuff
  addCompilerPlugin("org.typelevel"  %% "kind-projector"     % kindProjectorV cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe"                   %% "circe-refined"
    ).map(_ % circeVersion)
  )
