package popsicle.components

import popsicle.backend.websocket.{EchoWebSocket, WebSocket}
import popsicle.components.backend.websocket.WebSocketBackend

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}

import utest._

object WebSocketComponentTest extends TestSuite {

  class TestWebSocketComponent(backendFactory: BackendScope[_, String] => WebSocketBackend[String])
    extends WebSocketComponent[String](backendFactory) {

    import japgolly.scalajs.react._, vdom.all._

    override def initState = "initial"
    override def renderState(state: String) = div(`class` := "state", state)
  }

  def assertState(component: ComponentM, state: String): Unit = {
    val el = ReactTestUtils.findRenderedDOMComponentWithClass(component, "state")
    assert(el.getDOMNode().innerHTML == state)
  }

  val tests = TestSuite {
    'reactivePush {
      case class ReactivePush($: BackendScope[_, String], ws: WebSocket) extends WebSocketBackend($, ws) {
        import rx._, ops._
        ws.rxPush.foreach(msg => $.setState(msg), skipInitial = true)
      }

      val ws = new EchoWebSocket()
      val reactivePush = new TestWebSocketComponent($ => ReactivePush.apply($, ws))
      val component = ReactTestUtils.renderIntoDocument(reactivePush.buildComponent())
      assertState(component, "initial")
      ws.onReceive("push")
      assertState(component, "push")
    }
  }

}
