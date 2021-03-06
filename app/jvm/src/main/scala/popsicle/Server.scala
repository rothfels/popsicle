package popsicle

import popsicle.rpc.PollRPC
import popsicle.rpc.counter.{Counter, CounterRPCServer}
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
        script(`type`:="application/octet-stream", src:="/client-fastopt.js.map"),
        script(`type`:="text/javascript", src:="//localhost:12345/workbench.js"),
        script(`type`:="text/javascript", src:="http://cdnjs.cloudflare.com/ajax/libs/react/0.12.1/react-with-addons.min.js"),
        script(`type`:="text/javascript", src:="http://cdn.jsdelivr.net/jquery/2.1.1/jquery.js"),
        script(`type`:="text/javascript", src:="META-INF/resources/webjars/bootstrap/3.3.4/js/bootstrap.min.js"),
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="META-INF/resources/webjars/bootstrap/3.3.4/css/bootstrap.min.css"
        )
      ),
      body(margin := 0, paddingTop := 70)( // padding top: http://getbootstrap.com/components/#navbar-fixed-top
        script("popsicle.PopsicleApp().main()")
      )
    )
}

object AutowireServer extends autowire.Server[String, upickle.Reader, upickle.Writer] {
  def read[Result: upickle.Reader](p: String) = upickle.read[Result](p)
  def write[Result: upickle.Writer](r: Result) = upickle.write(r)
}

object Server extends SimpleRoutingApp with Counter.Server /* with MongoQueries, etc. */ {
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
              AutowireServer.route[CounterRPCServer](Server)(
                autowire.Core.Request(s, upickle.read[Map[String, String]](e))
              )
            }
          }
        }
      }
    }

    WebSocketServer.start()
  }
}
