ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

lazy val commons = (project in file("commons"))
  .settings(
    name := "commons"
  )

lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "frontend",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= { _.withSourceMap(true) },
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "0.13.1"
    ),
  )
  .dependsOn(commons)

lazy val scrapper = (project in file("scrapper"))
  .settings(
    name := "scrapper"
  )
  .dependsOn(commons)
