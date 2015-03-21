package popsicle

import japgolly.scalajs.react._, vdom.all._
import org.scalajs.dom
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import popsicle.pages._

object PopsicleApp extends JSApp {

  case class State(index: Int)

  case class Backend(t: BackendScope[_, State]) {
    def onMenuClick(newIndex: Int) = t.modState(_.copy(index = newIndex))
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

//import autowire._
//import org.scalajs.dom
//import upickle._
//
//import scala.concurrent.Future
//import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
//import scala.scalajs.js.JSApp
//import scala.scalajs.js.annotation.JSExport
//import scalatags.JsDom.all._
//
//object Client extends autowire.Client[String, upickle.Reader, upickle.Writer]{
//  override def doCall(req: Request): Future[String] = {
//    dom.ext.Ajax.post(
//      url = "/api/" + req.path.mkString("/"),
//      data = upickle.write(req.args)
//    ).map(_.responseText)
//  }
//
//  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
//  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
//}
//
//@JSExport
//object PopsicleApp {
//  @JSExport
//  def main(): Unit = {
//
//    val inputBox = input.render
//    val outputBox = div.render
//
//    def updateOutput() = {
//      Client[Api].list(inputBox.value).call().foreach { paths =>
//        outputBox.innerHTML = ""
//        outputBox.appendChild(
//          ul(
//            for(path <- paths) yield {
//              li(path)
//            }
//          ).render
//        )
//      }
//    }
//    inputBox.onkeyup = {(e: dom.Event) =>
//      updateOutput()
//    }
//    updateOutput()
//    dom.document.body.appendChild(
//      div(
//        cls:="container",
//        h1("File Browser"),
//        p("Enter a file path to s"),
//        inputBox,
//        outputBox
//      ).render
//    )
//  }
//}
