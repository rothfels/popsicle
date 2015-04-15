package popsicle.components.backend

import japgolly.scalajs.react.{ReactElement, ReactComponentB, BackendScope}

/**
 * This class represents a stateful react component backend.
 *
 * @param $ BackendScope injected by react component
 * @tparam State type of underlying react component state
 */
abstract class ComponentBackend[State]($: BackendScope[_, State]) {
  def init(): Unit
  def close(): Unit
}

abstract class BackendComponent[State](backendFactory: BackendScope[_, State] => ComponentBackend[State]) {

  def initState: State
  def renderState(state: State): ReactElement

  def buildComponent = ReactComponentB[Unit]("backend-component")
    .initialState(initState)
    .backend(backendFactory(_))
    .render($ => renderState($.state))
    .componentDidMount(_.backend.init())
    .componentWillUnmount(_.backend.close())
    .buildU
}
