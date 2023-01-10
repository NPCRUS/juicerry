ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

val circeVersion = "0.14.3"

val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)


lazy val commons = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("commons"))
  .settings(
    name := "commons",
  )
  .jvmSettings(
    libraryDependencies ++= circe
  )
  .jsSettings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion
    )
  )

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "frontend",
    scalacOptions ++= Seq(
      "-Xmax-inlines:256"
    ),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { _.withSourceMap(true) },
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "0.13.1"
    ),
  )
  .dependsOn(commons.js)

lazy val scrapper = (project in file("scrapper"))
  .settings(
    name := "scrapper",
    scalacOptions ++= Seq(
      "-Xmax-inlines:64"
    ),
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.5",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.6",
      "com.softwaremill.sttp.client3" %% "zio" % "3.8.6",
      "net.ruippeixotog" %% "scala-scraper" % "3.0.0"
    )
  )
  .dependsOn(commons.jvm)
