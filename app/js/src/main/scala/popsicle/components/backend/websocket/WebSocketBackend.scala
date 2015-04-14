package popsicle.components.backend.websocket

import rx._, ops._

import japgolly.scalajs.react.BackendScope
import popsicle.WebSocketPushRPC
import popsicle.backend.websocket.WebSocket
import popsicle.components.backend.ComponentBackend

abstract class WebSocketBackend[State]($: BackendScope[_, State], ws: WebSocket) extends ComponentBackend($) {
  override def init(): Unit = {
    ws.init()
  }

  override def close(): Unit = {
    ws.close()
  }
}

class WebSocketPushRPCBackend[State]($: BackendScope[_, State], ws: WebSocket, rpc: WebSocketPushRPC)
  extends WebSocketBackend($, ws) {

  override def init(): Unit = {
    super.init()
    ws.rxPush.foreach(msg => rpc.call(msg), skipInitial = true)
  }
}

//case class CounterState(counter: Int)
//class CounterSocketBackend($: BackendScope[_, CounterState], ws: WebSocket) extends WebSocketBackend($, ws) {
//  import popsicle.backend.reactive.Variables.counter
//  import rx._, ops._
//
//  counter.foreach { x =>
//    $.setState(CounterState(x))
//  }
//}