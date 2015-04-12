package popsicle.backend.websocket

import popsicle.WebSocketPushRPC

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object WebSocketPushHandler extends WebSocketPushRPC {

  def push(msg: String): Unit = {
    WebSocketAutowireReceiver.route[WebSocketPushRPC](WebSocketPushHandler)(
       upickle.read[autowire.Core.Request[String]](msg)
//      autowire.Core.Request(path, upickle.read[Map[String, String]](args))
    )
  }

  override def incrementCounter(name: String): autowire.Core.Request[String] = {
    println("foo")
    println(name)
    null
  }
}

object WebSocketAutowireReceiver extends autowire.Server[String, upickle.Reader, upickle.Writer] {
  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}