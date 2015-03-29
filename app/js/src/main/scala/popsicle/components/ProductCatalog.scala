package popsicle.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._
import popsicle.AjaxRpcClient
import popsicle.util.Util

import scala.scalajs.js

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object ProductCatalog {



  case class State(products: List[models.Product])

  class Backend($: BackendScope[_, State]) {
    var interval: js.UndefOr[js.timers.SetIntervalHandle] =
      js.undefined

    def tick() =
      $.setState(State(List(models.Product("a", "b", "c"), models.Product("d", "e", "h"))))
//      AjaxRpcClient.getProduct.foreach { productOption =>
//        productOption.foreach(product => $.modState(s => State(List(product))))
//      }

    def start() =
      tick()
//      interval = js.timers.setInterval(10000)(tick())
  }

  val ProductCatalog = ReactComponentB[Unit]("ProductCatalog")
    .initialState(State(Nil))
    .backend(new Backend(_))
    .render($ => {
      table(`class` := "table table-striped", "foo".reactAttr := "bar",
        thead(
          List("ID", "Name", "Producer").map(th(_))
        ),
        tbody(
          $.state.products.map(p => {
            tr(
              td(`class` := "info", p.id),
              td(`class` := "danger", p.name),
              td(`class` := "info", p.producer)
            )
          })
        )
      )
    })
    .componentDidMount(_.backend.start())
    .componentWillUnmount(_.backend.interval foreach js.timers.clearInterval)
    .buildU
}
