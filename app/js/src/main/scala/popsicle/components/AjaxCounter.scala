package popsicle.components

import japgolly.scalajs.react._, vdom.all._
import popsicle.AjaxRpcClient
import popsicle.components.backend.AjaxComponent

import scala.concurrent.Future

import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

object AjaxCounter {

  def component = ReactComponentB[Int]("ajax-counter")
    .render($ => {
      div($)
    })
    .build
}

class AjaxCounter extends AjaxComponent[Int] {
  import AjaxComponent.State

  override def initialState = State(0)

  override def refreshState: Future[Int] = AjaxRpcClient.getCounter

  override def stateComponent(state: Int) = AjaxCounter.component(state)

  override def refreshInterval = 10000
}