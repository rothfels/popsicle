import sbt.Keys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.RevolverPlugin.Revolver

val app = crossProject.settings(
  scalaVersion := "2.11.4",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.2.6",
    "com.lihaoyi" %%% "autowire" % "0.2.4",
    "com.lihaoyi" %%% "scalatags" % "0.4.5",
    "joda-time" % "joda-time" % "2.7",
    "org.scala-lang.modules" %% "scala-async" % "0.9.2"
  )
).jsSettings(
  workbenchSettings:_*
).jsSettings(
  name := "Client",
  jsDependencies += "org.webjars" % "react" % "0.12.1" / "react-with-addons.js" commonJSName "React",
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.github.japgolly.scalajs-react" %%% "core" % "0.8.2",
    "com.github.japgolly.scalajs-react" %%% "test" % "0.8.2" % "test"
  ),
  bootSnippet := "popsicle.PopsicleApp().main();"
).jvmSettings(
  Revolver.settings:_*
).jvmSettings(
  name := "Server",
  libraryDependencies ++= Seq(
    "io.spray" %% "spray-can" % "1.3.1",
    "io.spray" %% "spray-routing" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.2",
    "org.webjars" % "bootstrap" % "3.3.4",
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23"
  )
)

skip in packageJSDependencies := false

val appJS = app.js
val appJVM = app.jvm.settings(
  (resources in Compile) += {
    (fastOptJS in (appJS, Compile)).value
    (artifactPath in (appJS, Compile, fastOptJS)).value
  }
)
