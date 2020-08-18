name := "scala-type-level"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.typelevel" %% "cats-core" % "2.0.0",
  "org.typelevel" %% "cats-effect" % "2.1.4",
  //  "dev.zio"  %% "zio"               % "1.0.0-RC12-1",
//  "dev.zio"  %% "zio-interop-cats"  % "2.0.0.0-RC2"
)

scalacOptions += "-Ypartial-unification"
