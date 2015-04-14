package popsicle.components.backend

import japgolly.scalajs.react.BackendScope

/**
 * This class represents a stateful react component backend.
 *
 * @param $ BackendScope injected by react component
 * @tparam State type of underlying react component state
 */
abstract class ComponentBackend[State]($: BackendScope[_, State]) {
  def init(): Unit = {}
  def close(): Unit = {}
}
