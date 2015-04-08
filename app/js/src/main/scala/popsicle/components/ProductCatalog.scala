package popsicle.components

import japgolly.scalajs.react._, vdom.all._
import popsicle.AjaxRpcClient
import popsicle.components.backend.AjaxComponent

import scala.concurrent.Future

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object ProductCatalog {

  def component = ReactComponentB[List[models.Product]]("product-catalog")
    .render($ => {
      table(`class` := "table table-striped",
        thead(
          List("ID", "Name", "Producer").map(th(_))
        ),
        tbody(
          $.map(product => {
            tr(
              td(`class` := "info", product.id),
              td(`class` := "danger", product.name),
              td(`class` := "info", product.producer)
            )
          })
        )
      )
    })
    .build
}

class RefreshingProductCatalog extends AjaxComponent[List[models.Product]] {
  import AjaxComponent.State

  override def initialState = State(Nil)

  override def refreshState: Future[List[models.Product]] =
    AjaxRpcClient.getProduct.map(option => {
      // silly me...
      val nonOption = option.getOrElse(models.Product("a", "b", "c"))
      List(nonOption)
    })

  override def stateComponent(state: List[models.Product]) = ProductCatalog.component(state)

  override def refreshInterval = 10000
}
