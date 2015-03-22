package popsicle

import japgolly.scalajs.react._, vdom.all._
import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import popsicle.pages._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object PopsicleApp extends JSApp {

  case class State(index: Int)

  case class Backend(t: BackendScope[_, State]) {
    def onMenuClick(newIndex: Int) = {
      QueryClient.getProduct.foreach(println(_))
//      APIClient.getProduct.foreach(println(_))
//      APIClient.list("s").foreach(println(_))
      t.modState(_.copy(index = newIndex))
    }
  }

  val navMenu = ReactComponentB[(List[String], Backend)]("appMenu")
    .render(P => {
      val (data, b) = P
      def element(name: String, index: Int) =
        li(
          `class` := "navbar-brand",
          onClick --> b.onMenuClick(index),
          name
        )

      div(`class` := "navbar navbar-default",
        ul(`class` := "navbar-header",
          data.zipWithIndex.map { case (name, index) => element(name, index)}
        )
      )
    })
    .build

  val container = ReactComponentB[String]("appMenu")
    .render(P => {
      div(`class` := "container",
        P match {
          case "Home" => HomePage.content
          case "Examples" => ExamplesPage.component()
          case "Documentation" =>
            p("Please see the ",
              a(href := "https://github.com/goodeggs/popsicle", "project page"),
              "."
            )
        }
      )
    })
    .build

  val app = ReactComponentB[List[String]]("app")
    .initialState(State(0))
    .backend(new Backend(_))
    .render((P, S, B) => {
      div(
        navMenu((P, B)),
        container(P(S.index))
      )
    })
    .build

  def component(data: List[String]) =
    app(data) render dom.document.body

  @JSExport
  override def main(): Unit =
    component(List("Home", "Examples", "Documentation"))
}
