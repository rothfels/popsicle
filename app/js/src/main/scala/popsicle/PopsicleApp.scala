package popsicle

import japgolly.scalajs.react._, vdom.all._
import org.scalajs.dom
import popsicle.components.Navbar.NavbarData
import popsicle.components.{Navbar, Nav, ProductCatalog}
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import popsicle.pages._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object PopsicleApp extends JSApp {

  case class State(index: Int)

  case class Backend(t: BackendScope[_, State]) {
    def onMenuClick(newIndex: Int) = {
      AjaxRpcClient.getProduct.foreach(println(_))
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
          data.zipWithIndex.map {
            case (name, index) => element(name, index)
          }
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

  def component(data: List[String]) = {
    //    ProductCatalog.ProductCatalog() render dom.document.body
    import Nav._

    val data = NavData(
      List(
        NavState("foo", () => "foo"),
        NavState("baz", () => "baz"),
        NavState("bar", () => "bar")
      ),
      NavProps(NavPills)
    )
    val myNav = new Nav(data)
    myNav.nav render dom.document.body
    (new Navbar(NavbarData(null))).renderable render dom.document.body
    //    app(data) render dom.document.body
  }

  @JSExport
  override def main(): Unit = {
    component(List("Home", "Examples", "Documentation"))
  }
}
