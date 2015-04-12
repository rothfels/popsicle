package popsicle.components.backend.websocket

import japgolly.scalajs.react.BackendScope
import popsicle.backend.websocket.WebSocketModule


abstract class WebSocketBackend[Props, State]($: BackendScope[Props, State]) extends WebSocketModule {
  def init(): Unit = {
    webSocket.init()
  }

  def close(): Unit = {
    webSocket.close()
  }
}

//trait WebSocketBackendModule {
//  def backend: WebSocketBackend
//}