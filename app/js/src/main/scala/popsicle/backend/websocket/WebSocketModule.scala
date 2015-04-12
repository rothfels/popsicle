package popsicle.backend.websocket

trait WebSocket {
  def init(): Unit
  def onOpen(): Unit
  def onClose(): Unit
  def onError(msg: String): Unit
  def onReceive(msg: String): Unit
  def send(msg: String): Unit
  def close(): Unit
}

/**
 * This WebSocket implementation prints console logs
 * for each method invocation.
 */
class EchoWebSocket extends WebSocket {
  override def init(): Unit = {
    println("Socket init()")
  }

  override def onOpen(): Unit = {
    println("Socket onOpen() fired")
  }

  override def onClose(): Unit = {
    println("Socket onClose() fired")
  }

  override def onError(msg: String): Unit = {
    println(s"Socket onError() fired with msg ${msg}")
  }

  override def onReceive(msg: String): Unit = {
    println(s"Socket onReceive() fired with msg ${msg}")
  }

  override def send(msg: String): Unit = {
    println(s"Socket send(${msg})")
  }

  override def close(): Unit = {
    println("Socket close()")
  }
}

/**
 * Wrapper for org.scalajs.dom.WebSocket
 * @param address connection address, e.g. "ws://0.0.0.0:8081"
 */
class DomWebSocket(address: String) extends EchoWebSocket {
  import org.scalajs.dom

  val ws = new dom.WebSocket(address)

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
    ws.close() // close with status code?
  }
}

trait WebSocketModule {
  def webSocket: WebSocket
}
