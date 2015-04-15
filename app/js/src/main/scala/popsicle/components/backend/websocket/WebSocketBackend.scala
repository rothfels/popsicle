package popsicle.components.backend.websocket

import popsicle.rpc.PushRPC
import rx._, ops._

import japgolly.scalajs.react.BackendScope
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

class WebSocketPushRPCBackend[State]($: BackendScope[_, State], ws: WebSocket, rpc: PushRPC)
  extends WebSocketBackend($, ws) {

  var obs: rx.Obs = null

  override def init(): Unit = {
    super.init()
    obs = ws.rxPush.foreach(msg => rpc.call(msg), skipInitial = true)
  }
  override def close(): Unit = {
    super.close()
    obs.kill()
  }
}