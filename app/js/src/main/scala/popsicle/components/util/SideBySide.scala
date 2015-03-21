package popsicle.components.util

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object SideBySide {

  case class Content(jsSource: String, scalaSource: String, el: ReactElement) {
    def apply() = sideBySideComponent(this)
  }

  val sideBySideComponent = ReactComponentB[Content]("sideBySideExample")
    .render(p => {
      div(
        div(`class` := "row",
          div(`class` := "col-md-6",
            h3("JS source"),
            pre(code(p.jsSource.trim))
          ),
          div(`class` := "col-md-6",
            h3("Scala source"),
            pre(code(p.scalaSource.trim))
          )
        ),
        hr,
        section(cls := "demo",
          h3("Demo"),
          div(cls := "demo", p.el)
        )
      )
    })
    .build

}