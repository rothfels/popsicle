package popsicle.rpc

import autowire._
import org.scalajs.dom
import upickle._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

/**
 * PollRPC implementation via ajax post request.
 * The url is the fully qualified path of the method, e.g. http://0.0.0.0/api/popsicle/rpc/...
 * The body of the post is the method args.
 */
object AjaxClient extends autowire.Client[String, upickle.Reader, upickle.Writer] with PollRPC {
  override def doCall(req: Request): Future[String] = {
    println(req)
    println(req.path)
    println(req.args)
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.write(req.args)
    ).map(_.responseText)
  }

  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}

trait PushRPCAutowire {
  def parse(reqPickle: String) = {
    upickle.read[autowire.Core.Request[String]](reqPickle)
  }

  /**
   * "Server" config for receiving websocket push "requests".
   */
  object Autowire extends autowire.Server[String, upickle.Reader, upickle.Writer] {
    def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
    def write[Result: upickle.Writer](r: Result) = upickle.write(r)
  }
}

/**
 * Useful for testing; make push requests directly to PushRPCImpl
 * without a server or websocket.
 */
object PushRPCClient extends autowire.Client[String, upickle.Reader, upickle.Writer] {
  override def doCall(req: Request): Future[String] = {
    Future(write(req))
  }

  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}
