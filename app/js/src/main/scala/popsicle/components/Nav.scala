package popsicle.components

import japgolly.scalajs.react
import japgolly.scalajs.react._, vdom.all._
import org.scalajs.dom.raw.HTMLElement
import popsicle.components.Nav.NavData

object Nav {

  sealed abstract class NavType(val classString: String)
  case object NavTabs extends NavType("nav nav-tabs")
  case object NavPills extends NavType("nav nav-pills")
  case object NavStackedPills extends NavType("nav nav-pills nav-stacked")

  sealed abstract class NavJustification()
  case object NavNonJustified extends NavJustification
  case object NavJustified extends NavJustification

  case class NavProps(navType: NavType, justification: NavJustification = NavJustified) {
    def classString = justification match {
      case NavNonJustified => navType.classString
      case NavJustified => s"${navType.classString} nav-justified"
    }
  }

  // Navs should be constructed with a non-empty list of NavState.
  // Each NavState.component is a no-arg function which should
  // return a valid DOM value, e.g.
  //   => (scalatags) div(...)
  //   => (React) component()
  //   => plain text
  case class NavState(name: String, component: () => TagMod)
  case class NavData(states: List[NavState], props: NavProps)

  case class State(i: Int)

  class Backend($: BackendScope[_, State]) {
    def go(i: Int): Unit = {
      $.setState(State(i))
    }
  }
}

class Nav(data: NavData) {
  import Nav._

  // renderable
  val nav = component(data)

  // for testing; set when component rendered
  var _navRefs: List[react.Ref] = null

  def component = ReactComponentB[NavData]("nav")
    .initialState(State(0))
    .backend(new Backend(_))
    .render((data, state, backend) => {
      val (navStates, navProps) = (data.states, data.props)

      if (navStates.size == 0) {
        div(
          "Nil nav states"
        )
      }
      else {
        div(`class` := "well",
          navMenu((navStates.map(_.name), navProps, state, backend)),
          div(`class` := "panel panel-default",
            div(`class` := "panel-body", navStates(state.i).component())
          )
        )
      }
    })
    .build

  def navMenu = ReactComponentB[(List[String], NavProps, State, Backend)]("navMenu")
    .render($ => {
      val (navStates, navProps, state, backend) = $

      _navRefs = navStates.map(Ref[HTMLElement](_))

      ul(`class` := navProps.classString,
        navStates.zipWithIndex.map(tuple => {
          val (navStateName: String, i: Int) = tuple
          li(
            `class` := (if (i == state.i) "active" else ""),
            role := "presentation",
            onClick --> backend.go(i),
            ref := _navRefs(i),
            a(href := "#", navStateName)
          )
        })
      )
    })
    .build
}