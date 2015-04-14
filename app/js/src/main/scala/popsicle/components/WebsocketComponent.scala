package popsicle.components

import popsicle.components.backend.websocket.WebSocketBackend

import japgolly.scalajs.react.{ReactElement, BackendScope, ReactComponentB}

abstract class WebSocketComponent[State](backendFactory: BackendScope[_, State] => WebSocketBackend[State]) {

  def initState: State
  def renderState(state: State): ReactElement

  def buildComponent = ReactComponentB[Unit]("websocket-component")
    .initialState(initState)
    .backend(backendFactory(_))
    .render($ => renderState($.state))
    .componentDidMount(_.backend.init())
    .componentWillUnmount(_.backend.close())
    .buildU
}
