package popsicle.backend.websocket

trait WebSocket {
  import rx._

  val rxPush = Var[String](null)

  def init(): Unit

  def send(msg: String): Unit
  def close(): Unit

  def onOpen(): Unit
  def onClose(): Unit
  def onError(msg: String): Unit
  def onReceive(msg: String): Unit = {
    rxPush() = msg
  }
}

/**
 * This WebSocket implementation prints console logs
 * for each method invocation conditioned on "echo" boolean flag.
 */
class EchoWebSocket extends WebSocket {
  val echo = true

  override def init(): Unit = {
    if (echo) println("Socket init()")
  }

  override def send(msg: String): Unit = {
    if (echo) println(s"Socket send(${msg})")
  }

  override def close(): Unit = {
    if (echo) println("Socket close()")
  }

  override def onOpen(): Unit = {
    if (echo) println("Socket onOpen() fired")
  }

  override def onClose(): Unit = {
    if (echo) println("Socket onClose() fired")
  }

  override def onError(msg: String): Unit = {
    if (echo) println(s"Socket onError() fired with msg ${msg}")
  }

  override def onReceive(msg: String): Unit = {
    if (echo) println(s"Socket onReceive() fired with msg ${msg}")
    super.onReceive(msg)
  }
}

import org.scalajs.dom

/**
 * Wrapper for org.scalajs.dom.WebSocket
 */
class DomWebSocket(ws: dom.WebSocket) extends EchoWebSocket {

  override def init(): Unit = {
    super.init()

    import dom._
    ws.onmessage = (e: MessageEvent) => onReceive(e.data.toString)
    ws.onopen = (e: Event) => onOpen()
    ws.onerror = (e: ErrorEvent) => onError(e.message)
    ws.onclose = (e: CloseEvent) => onClose()
  }

  override def send(msg: String): Unit = {
    super.send(msg)
    ws.send(msg)
  }
  
  override def close(): Unit = {
    super.close()
    ws.close()// close with status code?
  }
}