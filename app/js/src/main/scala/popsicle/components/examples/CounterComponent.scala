package popsicle.components.examples

import autowire.Core.Request
import japgolly.scalajs.react.BackendScope
import popsicle.rpc.{PushRPC, AjaxClient, PushRPCAutowire}
import popsicle.rpc.counter.{CounterRPCServer, CounterRPCClient}
import popsicle.websocket.WebSocket
import popsicle.websocket.DomWebSocket
import popsicle.components.backend._

import scala.concurrent.Future

import autowire._
import rx._


import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object Counter {

  val rxCounter = Var(0)

  trait Client extends CounterRPCClient {
    def incrementCounter: Request = {
      println("increment counter")
      rxCounter() = rxCounter() + 1
      null
    }
  }

  trait Backend {
    def getCounter: Future[Int]
    def refreshCounter = getCounter.foreach(rxCounter() = _)
    def ajax = getCounter
  }

  /**
   * PushRPC route-able interface.
   */
  object CounterRPC extends Client with PushRPCAutowire {
    def call(reqPickle: String): Unit = {
      Autowire.route[CounterRPCClient](CounterRPC)(parse(reqPickle))
    }
  }
}


case class CounterWebSocketBackend($: BackendScope[_, Int], ws: WebSocket, client: CounterRPCClient)
  extends WebSocketPushRPCBackend($, ws, client) {
}

case class CounterAjaxBackend($: BackendScope[_, Int]) extends AjaxBackend($) with Counter.Backend {
  def getCounter = AjaxClient[CounterRPCServer].getCounter.call()
}

case class CounterBackend($: BackendScope[_, Int], ws: WebSocket) extends Backend($) {
  val wsBackend = CounterWebSocketBackend($, ws, Counter.CounterRPC)
  val ajaxBackend = CounterAjaxBackend($)

  override def init(): Unit = {
    wsBackend.init()
    ajaxBackend.init()
  }

  override def close(): Unit = {
    wsBackend.close()
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

case class CounterComponent(ws: WebSocket) extends BackendComponent[Int](CounterBackend.apply(_, ws)) {
  import japgolly.scalajs.react.vdom.all._

  override def initState = 0
  override def renderState(state: Int) = div("counter: " + state)
  override def componentName = "counter"
}