package popsicle.components

import japgolly.scalajs.react.vdom.all._
import japgolly.scalajs.react.{BackendScope, ReactComponentB}
import org.scalajs.dom
import org.scalajs.dom._
import popsicle.backend.websocket.DomWebSocket
import popsicle.components.examples.{CounterWebSocket, CounterComponent, CounterWebSocketBackend}

//import popsicle.backend.websocket.WebSocketPushRPCImpl
import popsicle.rpc.counter.{Counter}

object WebsocketComponentOld {
//  case class State[T](s: T)
  case class State(msg: String)
}

import popsicle.components.WebsocketComponentOld._

class WebsocketComponentOld {
//  def initialState: State // initial component state
//  def stateComponent(state: T): TagMod // how to render the state, e.g. div(...)

  val ws = new dom.WebSocket("ws://0.0.0.0:8081")

  def component = CounterComponent(CounterWebSocket(ws)).buildComponent()

//  val ws = new dom.WebSocket("ws://0.0.0.0:8081")
//  val component = new WebSocketComponent[State]() {}
//
//  class Backend($: BackendScope[_, State]) {
//    val ws = new dom.WebSocket("ws://0.0.0.0:8081")
//
//    def init(): Unit = {
//      ws.onmessage = (x: MessageEvent) => {
//        println("got a message! " + x.data.toString)
//        $.setState(State(x.data.toString))
//        Counter.Client.call(x.data.toString)
//      }
//      ws.onopen = (x: Event) => {
//        println("opened the websocket")
//        ws.send("hello server")
//      }
//      ws.onerror = (x: ErrorEvent) => println("some error has occurred " + x.message)
//      ws.onclose = (x: CloseEvent) => {}
//    }
//
//    def close(): Unit = {
//      ws.close()
//    }
//  }
////}
//
//  def component = ReactComponentB[Unit]("websocket-component")
//    .initialState(State("initial"))
//    .backend(new Backend(_))
//    .render($ => div($.state.msg))
//    .componentDidMount(_.backend.init())
//    .componentWillUnmount(_.backend.close())
//    .buildU
}