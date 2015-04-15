package popsicle.rpc.counter

import autowire._
import popsicle.rpc.{PushRPCAutowire, AjaxClient}

import rx._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object Counter {
  val rxCounter = Var(0)

  object Client extends CounterRPCClient with PushRPCAutowire {
    /**
     * Invoke WebSocketPushRPC method by pickle'd request.
     * This is the entry point for an auto-wired method RPC
     * delivered by server push over websocket.
     *
     * @param reqPickle pickled autowire Core.Request
     */
    def call(reqPickle: String): Unit = {
      Autowire.route[CounterRPCClient](Client)(parse(reqPickle))
    }

    override def incrementCounter: Request = {
      println("increment counter")
      rxCounter() = rxCounter() + 1
      null
    }
  }

  object Server {
    def getCounter: Future[Int] = AjaxClient[CounterRPCServer].getCounter.call()
    def refreshCounter = getCounter.foreach(rxCounter() = _)
  }
}
