package popsicle.components.examples

import japgolly.scalajs.react.BackendScope
import popsicle.backend.websocket.{DomWebSocket, WebSocket}
import popsicle.components.backend.{BackendComponent, ComponentBackend}
import popsicle.components.backend.ajax.AjaxBackend
import popsicle.components.backend.websocket.WebSocketPushRPCBackend
import popsicle.rpc.counter.Counter

import scala.concurrent.Future

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow


case class CounterWebSocketBackend($: BackendScope[_, Int], ws: WebSocket)
  extends WebSocketPushRPCBackend($, ws, Counter.Client) {
}

case class CounterAjaxBackend($: BackendScope[_, Int]) extends AjaxBackend($) {
  override def ajaxFn: Future[Int] = {
    println("doing ajax!")
    Counter.Server.getCounter.map { x =>
      println("ajax returned: " + x)
      x
    }
  }
}

case class CounterBackend($: BackendScope[_, Int], ws: WebSocket) extends ComponentBackend($) {
  val wsBackend = CounterWebSocketBackend($, ws)
  val ajaxBackend = CounterAjaxBackend($)

  override def init(): Unit = {
//    wsBackend.init()
    ajaxBackend.init()
  }

  override def close(): Unit = {
//    wsBackend.close()
    ajaxBackend.close()
  }
}

case class CounterWebSocket(ws: org.scalajs.dom.WebSocket) extends DomWebSocket(ws) {
  override def onOpen(): Unit = {
    super.onOpen()
    send("hello, server")
  }

  override def onReceive(msg: String): Unit = {
    super.onReceive(msg)
    println("done with on receive")
  }
}

case class CounterComponent(ws: WebSocket) extends BackendComponent[Int]($ => CounterBackend.apply($, ws)) {
  import japgolly.scalajs.react.vdom.all._

  override def initState = 0
  override def renderState(state: Int) = div("counter: " + state)
}
