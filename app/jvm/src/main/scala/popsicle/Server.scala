package popsicle

import upickle._

import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.{MediaTypes, HttpEntity}
import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem

import popsicle.db.queries.MongoQueryRPC

object Template {
  import scalatags.Text.all._
  import scalatags.Text.tags2.title
  val txt =
    "<!DOCTYPE html>" +
    html(
      head(
        title("Popsicle"),
        meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
        script(`type`:="text/javascript", src:="/client-fastopt.js"),
        script(`type`:="text/javascript", src:="//localhost:12345/workbench.js"),
        script(`type`:="text/javascript", src:="http://cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react-with-addons.min.js"),
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="META-INF/resources/webjars/bootstrap/3.3.4/css/bootstrap.min.css"
        )
      ),
      body(margin:=0)(
        script("popsicle.PopsicleApp().main()")
      )
    )
}

object AutowireServer extends autowire.Server[String, upickle.Reader, upickle.Writer] {
  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}

object Server extends SimpleRoutingApp with MongoQueryRPC {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    startServer("0.0.0.0", port = 8080) {
      get{
        pathSingleSlash {
          complete{
            HttpEntity(
              MediaTypes.`text/html`,
              Template.txt
            )
          }
        } ~
        getFromResourceDirectory("")
      } ~
      post {
        path("api" / Segments){ s =>
          extract(_.request.entity.asString) { e =>
            complete {
              AutowireServer.route[PopsicleRPC](Server)(
                autowire.Core.Request(s, upickle.read[Map[String, String]](e))
              )
            }
          }
        }
      }
    }
  }

}
