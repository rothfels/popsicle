package popsicle.components.bootstrap.navigation

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import popsicle.components.backend.{NavRouterBackend, NavComponent}

object NavbarComponent {

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
  case class NavbarState(name: String, buildComponent: () => ReactElement)
  case class NavbarProps(
    brand: String,
    states: List[NavbarState],
    navbarClass: NavbarClass = NavbarClass(List(NavbarDefault, NavbarFixedTop))
  )
  
  object Menu {
    private var menuId = 0
    def buildComponent = {
      menuId += 1 // unique id for each navbar

      ReactComponentB[(NavbarProps, Int, NavRouterBackend[Int])]("navbar-menu").render($ => {
        val (navbarData, state, backend) = $

        nav(`class` := navbarData.navbarClass.classString,
          role := "navigation",
          div(`class` := "container",
            div(`class` := "navbar-header",
              button(`class` := "navbar-toggle collapsed",
                `type` := "button",
                "data-toggle".reactAttr := "collapse",
                "data-target".reactAttr := s"#bs-example-navbar-collapse-${menuId}",
                span(`class`:= "sr-only", "Toggle navigation"),
                span(`class` := "icon-bar"),
                span(`class` := "icon-bar"),
                span(`class` := "icon-bar")
              ),
              a(`class` := "navbar-brand", href := "#", navbarData.brand)
            ),
            div(`class` := "collapse navbar-collapse",
              id := s"bs-example-navbar-collapse-${menuId}",
              ul(`class` := "nav navbar-nav",
                navbarData.states.zipWithIndex.map(tuple => {
                  val (navbarState: NavbarState, i: Int) = tuple
                  li(
                    `class` := (if (i == state) "active" else ""),
                    role := "presentation",
                    onClick --> backend.go(i),
                    a(href := "#", navbarState.name)
                  )
                })
              )
            )
          )
        )
      })
      .build
    }
  }
}

import popsicle.components.bootstrap.navigation.NavbarComponent._

case class NavbarComponent() extends NavComponent[NavbarProps, Int](NavRouterBackend.apply) {

  override def initState = 0
  override def componentName = "navbar-component"

  override def renderState(props: NavbarProps, i: Int, backend: NavRouterBackend[Int]) = {
    props.states.size match {
      case 0 => throw new Exception("Cannot construct nav component with nil states.")
      case _ => div(
        Menu.buildComponent((props, i, backend)),
        div(
          `class` := "container",
          div(props.states(i).buildComponent())
        )
      )
      //    }
      //    if (props.states.size == 0) {
      //      div("Nil nav states")
      //    }
      //    else {
      //      div(
      //        Menu.buildComponent((props, i, backend)),
      //        div(`class` := "container",
      //          div(props.states(i).buildComponent())
      //        )
      //      )
      //    }
    }
  }
}