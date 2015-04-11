package popsicle.components.backend

import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}

import scala.concurrent.Future
import scala.scalajs.js
import org.scalajs.dom
import org.scalajs.dom._

object WebsocketComponent {
//  case class State[T](s: T)
  case class State(msg: String)
}

import WebsocketComponent._

class WebsocketComponent {
//  def initialState: State // initial component state
//  def stateComponent(state: T): TagMod // how to render the state, e.g. div(...)

  class Backend($: BackendScope[_, State]) {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
    val ws = new dom.WebSocket("ws://0.0.0.0:8081")

    def init(): Unit = {
      ws.onmessage = (x: MessageEvent) => {
        println("got a message! " + x.data.toString)
        $.setState(State(x.data.toString))
      }
      ws.onopen = (x: Event) => {
        println("opened the websocket")
        ws.send("hello server")
      }
      ws.onerror = (x: ErrorEvent) => println("some error has occurred " + x.message)
      ws.onclose = (x: CloseEvent) => {}
    }

    def close(): Unit = {
      ws.close()
    }
  }
//}

  def component = ReactComponentB[Unit]("websocket-component")
    .initialState(State("initial"))
    .backend(new Backend(_))
    .render($ => div($.state.msg))
    .componentDidMount(_.backend.init())
    .componentWillUnmount(_.backend.close())
    .buildU
}