package popsicle.components.bootstrap.layout

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object SingleSide {

  case class Content(scalaSource: String, el: ReactElement) {
    def apply() = singleSideComponent(this)
  }

  val singleSideComponent = ReactComponentB[Content]("singleSideComponent")
    .render(p => {
      div(
        div(`class` := "row",
          div(`class` := "col-md-10",
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