package popsicle.components

import japgolly.scalajs.react
import japgolly.scalajs.react._, japgolly.scalajs.react.vdom.all._
import org.scalajs.dom.raw.HTMLElement
import popsicle.components.Nav.NavData

object Navbar {

  sealed abstract class NavbarType(val classString: String)
  case object NavbarDefault extends NavbarType("navbar-default")
  case object NavbarFixedTop extends NavbarType("navbar-fixed-top")
  case object NavbarFixedBottom extends NavbarType("navbar-fixed-bottom")

  case class NavbarClass(navbarTypes: List[NavbarType]) {
    def classString = "navbar " + navbarTypes.map(_.classString).mkString(" ")
  }

  // Navs should be constructed with a non-empty list of NavState.
  // Each NavState.component is a no-arg function which should
  // return a valid DOM value, e.g.
  //   => (scalatags) div(...)
  //   => (React) component()
  //   => plain text
  case class NavbarState(name: String, component: () => TagMod)
  case class NavbarData(
    states: List[NavbarState],
    navbarClass: NavbarClass = NavbarClass(List(NavbarDefault, NavbarFixedTop))
  )

  case class State(i: Int)

  class Backend($: BackendScope[_, State]) {
    def go(i: Int): Unit = {
      $.setState(State(i))
    }
  }
}

import Navbar._

class Navbar(data: NavbarData) {

  val renderable = component(data)

  def component = ReactComponentB[NavbarData]("navbar")
    .initialState(State(0))
    .backend(new Backend(_))
    .render((data, state, backend) => {
//      val (navStates, navProps) = (data.states, data.props)

      if (false) {
        div(
          "Nil nav states"
        )
      }
      else {
        div(`class` := data.navbarClass.classString,
          role := "navigation",
          div(`class` := "container-fluid",
            div(`class` := "navbar-header",
              button(`class` := "navbar-toggle collapsed",
                `type` := "button",
                "data-toggle".reactAttr := "collapse",
                "data-target".reactAttr := "#bs-example-navbar-collapse-1",
                span(`class`:= "sr-only", "Toggle navigation"),
                span(`class` := "icon-bar"),
                span(`class` := "icon-bar"),
                span(`class` := "icon-bar")
              ),
              a(`class` := "navbar-brand", href := "#", "Brand")
            ),
            div(`class` := "collapse navbar-collapse",
              id := "bs-example-navbar-collapse-1",
              ul(`class` := "nav navbar-nav",
                li(`class` := "active",
                  a(href := "#", "Link ", span(`class` := "sr-only", "(current)"))
                ),
                li(
                  a(href := "#", "Link")
                ),
                li(`class` := "dropdown",
                  a(`class` := "dropdown-toggle",
                    "data-toggle".reactAttr := "dropdown",
                    role := "button",
                    "aria-expanded".reactAttr := "false",
                    "Dropdown ",
                    span(`class` := "caret")
                  ),
                  ul(`class` := "dropdown-menu",
                    role := "menu",
                    li(a(href := "#", "Action")),
                    li(a(href := "#", "Another action")),
                    li(a(href := "#", "Something elase here")),
                    li(`class` := "divider"),
                    li(a(href := "#", "Separated link")),
                    li(`class` := "divider"),
                    li(a(href := "#", "One more separated link"))
                  )
                )
              )
            )
          )
  //        navMenu((navStates.map(_.name), navProps, state, backend)),
  //        div(`class` := "panel panel-default",
  //          div(`class` := "panel-body", navStates(state.i).component())
  //        )
        )
      }
    })
    .build
//
//  def navbarMenu = ReactComponentB[(List[String], NavbarProps, State, Backend)]("navbarMenu")
//    .render($ => {
//      val (navStates, navProps, state, backend) = $
//
//      ul(`class` := navProps.classString,
//        navStates.zipWithIndex.map(tuple => {
//          val (navStateName: String, i: Int) = tuple
//          li(
//            `class` := (if (i == state.i) "active" else ""),
//            role := "presentation",
//            onClick --> backend.go(i),
//            a(href := "#", navStateName)
//          )
//        })
//      )
//    })
//    .build
}