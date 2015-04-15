package popsicle

import akka.actor.{ActorSystem, Actor, Props, ActorLogging, ActorRef, ActorRefFactory}
import akka.io.IO
import popsicle.rpc.counter.Counter
import spray.can.Http
import spray.can.server.UHttp
import spray.can.websocket
import spray.can.websocket.frame.{BinaryFrame, TextFrame}
import spray.http.HttpRequest
import spray.can.websocket.FrameCommandFailed
import spray.routing.HttpServiceActor

/**
 * Foundational server-generated message.
 * Send a Push to any WebSocketWorker actor and the message
 * will be delivered to the underlying client websocket connection.
 * @param msg message to send to connected client
 */
final case class Push(msg: String)

object WebSocketServer {
  def props() = Props(classOf[WebSocketServer])

  def start(): Unit = {
    implicit val system = ActorSystem()
    val server = system.actorOf(WebSocketServer.props(), "websocket")
    IO(UHttp) ! Http.Bind(server, "0.0.0.0", 8081)
  }
}

class WebSocketServer extends Actor with ActorLogging {
  def receive = {
    // When a new connection arrives, register a WebSocketConnection actor
    // as the per connection handler.
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()
      val conn = context.actorOf(WebSocketWorker.props(serverConnection))
      serverConnection ! Http.Register(conn)
  }
}

object WebSocketWorker {
  def props(serverConnection: ActorRef) = Props(classOf[WebSocketWorker], serverConnection)
}

class WebSocketWorker(val serverConnection: ActorRef) extends HttpServiceActor with websocket.WebSocketServerWorker {
  import akka.pattern.pipe
  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive = handshaking orElse businessLogicNoUpgrade orElse closeLogic

  def businessLogic: Receive = {

    // A binary or text message received from the client.
    // Currently "pongs" the message directly back to the client.
    case x @ (_: BinaryFrame | _: TextFrame) =>
      log.error("received push message")
      Counter.Client.incrementCounter
        .map(req => TextFrame(upickle.write(req)))
        .pipeTo(sender())

    // Send an async server-generated message the client.
    case Push(msg) =>
      send(TextFrame(msg))

    case x: FrameCommandFailed =>
      log.error("Frame command failed.", x)

    case x: HttpRequest => // do something
  }

  def businessLogicNoUpgrade: Receive = {
    implicit val refFactory: ActorRefFactory = context
    runRoute {
      complete("hello world")
      // getFromResourceDirectory("")
    }
  }
}