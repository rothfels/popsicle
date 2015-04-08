package popsicle.components.backend

import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}

import scala.concurrent.Future
import scala.scalajs.js

object AjaxComponent {
  case class State[T](s: T)
}

abstract class AjaxComponent[T] {
  import AjaxComponent._

  def initialState: State[T] // initial component state
  def refreshState: Future[T] // method to refresh state, e.g. AjaxRpcClient.rpc
  def stateComponent(state: T): TagMod // how to render the state, e.g. div(...)

  def refreshInterval = 0 // millis; how often to refresh state (0 = once when component mounted)

  class Backend($: BackendScope[_, State[T]]) {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

    var interval: js.UndefOr[js.timers.SetIntervalHandle] =
      js.undefined

    def refresh() = refreshState.foreach { value =>
      $.setState(State(value))
    }

    def start() = {
      refresh()
      if (refreshInterval > 0) {
        interval = js.timers.setInterval(refreshInterval)(refresh())
      }
    }
  }

  def component = ReactComponentB[Unit]("ajax-component")
    .initialState(initialState)
    .backend(new Backend(_))
    .render($ => div(stateComponent($.state.s)))
    .componentDidMount(_.backend.start())
    .componentWillUnmount(_.backend.interval foreach js.timers.clearInterval)
    .buildU
}
