package popsicle.rpc

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object PushRPCClient extends autowire.Client[String, upickle.Reader, upickle.Writer] {
  override def doCall(req: Request): Future[String] = {
    Future(write(req))
  }

  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}