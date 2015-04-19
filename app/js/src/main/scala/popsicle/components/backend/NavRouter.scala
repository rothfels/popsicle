package popsicle.components.backend

import japgolly.scalajs.react.BackendScope

trait NavRouter[State] {
  def go(state: State): Unit
}

case class NavRouterBackend[State]($: BackendScope[_, State]) extends Backend($) with NavRouter[State] {
  override def init(): Unit = {}
  override def close(): Unit = {}

  override def go(state: State): Unit = {
    $.setState(state)
  }
}

abstract class NavComponent[Props, State](backendFactory: BackendScope[_, State] => NavRouterBackend[State])
  extends BackendComponentPB[Props, State, NavRouterBackend[State]](backendFactory) {

  override def componentName = "navigation-component"
}