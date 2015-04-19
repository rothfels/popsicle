package popsicle.components.util

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}
import popsicle.components.backend.{BackendComponent, Backend, AjaxComponent, AjaxBackend}

case class TestStringBackendComponent(backendFactory: BackendScope[_, String] => Backend)
  extends BackendComponent[String](backendFactory) {

  import japgolly.scalajs.react._, vdom.all._

  override def initState = "initial"
  override def renderState(state: String) = div(`class` := "state", state)
  override def componentName = "test-string-backend-component"
}

object TestStringBackendComponent {
  // component is a mounted react component
  def assertState(component: ComponentM, state: String): Unit = {
    val el = ReactTestUtils.findRenderedDOMComponentWithClass(component, "state")
    assert(el.getDOMNode().innerHTML == state)
  }
}


