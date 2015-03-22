package popsicle

import org.scalajs.dom

import autowire._
import upickle._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object Client extends autowire.Client[String, upickle.Reader, upickle.Writer] {
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

object APIClient {
//  def list(path: String): Future[Seq[String]] = Client[Api].list(path).call()
  def getProduct: Future[models.Product] = Client[Api].getProduct.call()
}

object QueryClient {
  //  def list(path: String): Future[Seq[String]] = Client[Api].list(path).call()
  def getProduct: Future[models.Product] = Client[Queries].getFlowduct.call()
}