package popsicle.components.counter

import popsicle.components.util.TestStringBackendComponent
import popsicle.websocket.WebSocket
import popsicle.websocket.EchoWebSocket
import popsicle.components.backend.{WebSocketPushRPCBackend, WebSocketComponent, WebSocketBackend}

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}

import utest._

object CounterComponentTest extends TestSuite {

  case class ReactivePushBackend($: BackendScope[_, String], ws: WebSocket) extends WebSocketBackend($, ws) {
    import rx._, ops._
    ws.rxPush.foreach(msg => $.setState(msg), skipInitial = true)
  }

  val tests = TestSuite {
    'rxPushShouldFireWhenWebSocketReceivesMessage {
      val ws = new EchoWebSocket()
      val reactivePush = TestStringBackendComponent(ReactivePushBackend.apply(_, ws))
      val component = ReactTestUtils.renderIntoDocument(reactivePush.buildComponent())

      TestStringBackendComponent.assertState(component, "initial")
      ws.onReceive("push")
      TestStringBackendComponent.assertState(component, "push")
    }
  }

}

