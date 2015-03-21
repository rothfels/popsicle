import sbt.Keys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.RevolverPlugin.Revolver

val popsicle = crossProject.settings(
  scalaVersion := "2.11.4",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.2.6",
    "com.lihaoyi" %%% "autowire" % "0.2.4",
    "com.lihaoyi" %%% "scalatags" % "0.4.5"
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
    "org.webjars" % "bootstrap" % "3.3.4"
  )
)

skip in packageJSDependencies := false

val popsicleJS = popsicle.js
val popsicleJVM = popsicle.jvm.settings(
  (resources in Compile) += {
    (fastOptJS in (popsicleJS, Compile)).value
    (artifactPath in (popsicleJS, Compile, fastOptJS)).value
  }
)
