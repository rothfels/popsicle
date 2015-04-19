package popsicle

import japgolly.scalajs.react._, vdom.all._
import org.scalajs.dom
import popsicle.components.bootstrap.navigation.NavbarComponent, NavbarComponent._
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import popsicle.components.pages._

object PopsicleApp extends JSApp {

  @JSExport
  override def main(): Unit = {
    val navbarProps = NavbarProps(
      "Popsicle",
      List(
        NavbarState("Home", () => HomePage.content),
        NavbarState("Examples", () => ExamplesPage.component()),
        NavbarState("Documentation", () => p("Please see the ",
          a(href := "https://github.com/rothfels/popsicle", "project page"),
          "."
        ))
      )
    )

    NavbarComponent().buildComponent(navbarProps) render dom.document.body
  }
}
