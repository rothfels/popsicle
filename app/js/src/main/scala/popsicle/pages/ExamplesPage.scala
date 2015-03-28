package popsicle.pages

import japgolly.scalajs.react._, vdom.all._
import popsicle.components.examples._
import popsicle.components.util._

sealed abstract class Example(val title: String, val el: () => ReactElement)
object Example {
  implicit private def auto1(v: SideBySide.Content): () => ReactElement = () => v()
  implicit private def auto2(v: SingleSide.Content): () => ReactElement = () => v()

  case object Hello        extends Example("Hello World",        HelloMessageExample.content)
  case object Timer        extends Example("Timer",              TimerExample.content)
  case object Todo         extends Example("Todo List",          TodoExample.content)
  case object Refs         extends Example("Using Refs",         RefsExample.content)
  case object ProductTable extends Example("Product Table",      ProductTableExample.content)
  case object Animation    extends Example("Animation",          AnimationExample.content)

  val values = Vector[Example](Hello, Timer, Todo, Refs, ProductTable, Animation)
}

object ExamplesPage {

  case class State(current: Example)

  class Backend(t: BackendScope[_, State]) {
    def onMenuClick(tgt: Example) = t.setState(State(tgt))
  }

  val examplesMenu = ReactComponentB[(Backend, Example)]("examplesMenu")
    .render(P => {
      val (b, current) = P
      def element(e: Example) = {
        val active = e.toString == current.toString
        li(
          classSet1("list-group-item", "active" -> active),
          onClick --> b.onMenuClick(e),
          e.title
        )
      }
      div(`class` := "col-md-2",
        ul(`class` := "list-group",
          Example.values.map(element)
        )
      )
    })
    .build

  val exampleBody = ReactComponentB[Example]("examplesBody")
    .render(eg => div(`class` := "col-md-10", eg.el()))
    .build

  val component = ReactComponentB[Unit]("examplesPage")
    .initialState(State(Example.Hello))
    .backend(new Backend(_))
    .render((_, s, b) => {
      div(`class` := "row",
        examplesMenu((b, s.current)),
        exampleBody(s.current)
      )
    })
    .buildU
}