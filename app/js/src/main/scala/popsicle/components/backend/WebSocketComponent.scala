package popsicle.components.backend

import popsicle.websocket.WebSocket
import popsicle.rpc.PushRPC
import rx._, ops._

import japgolly.scalajs.react.BackendScope

abstract class WebSocketBackend[State]($: BackendScope[_, State], ws: WebSocket) extends Backend($) {
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

abstract class WebSocketComponent[State](backendFactory: BackendScope[_, State] => WebSocketBackend[State])
  extends BackendComponent(backendFactory) {

  override def componentName = "websocket-component"
}
