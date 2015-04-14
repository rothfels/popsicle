package popsicle.backend.websocket

import popsicle.WebSocketPushRPC
import popsicle.backend.reactive.Variables._

object WebSocketPushRPCImpl extends WebSocketPushRPC {
  import scala.concurrent.ExecutionContext.Implicits.global

  /**
   * Invoke WebSocketPushRPC method by pickle'd request.
   * This is the entry point for an auto-wired method RPC
   * delivered by server push over websocket.
   *
   * @param reqPickle pickled autowire Core.Request
   */
  def call(reqPickle: String): Unit = {
    Autowire.route[WebSocketPushRPC](WebSocketPushRPCImpl)(
       upickle.read[autowire.Core.Request[String]](reqPickle)
    )
  }

  /**
   * "Server" config for receiving websocket push "requests".
   */
  object Autowire extends autowire.Server[String, upickle.Reader, upickle.Writer] {
    def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
    def write[Result: upickle.Writer](r: Result) = upickle.write(r)
  }

  override def incrementCounter: PushResponse = {
    println("increment counter")
    counter() = counter() + 1
    null
  }
}
