//package popsicle.components.examples
//
//import japgolly.scalajs.react._
//import japgolly.scalajs.react.vdom.all._
//import popsicle.backend.ajax.AjaxRpcClient
//import popsicle.components.AjaxComponent
//
//import scala.concurrent.Future
//
//object AjaxCounter {
//
//  def component = ReactComponentB[Int]("ajax-counter")
//    .render($ => {
//      div($)
//    })
//    .build
//}
//
//class AjaxCounter extends AjaxComponent[Int] {
//  import AjaxComponent.State
//
//  override def initState = State(0)
//
//  override def refreshState: Future[Int] = AjaxRpcClient.getCounter
//
//  override def stateComponent(state: Int) = AjaxCounter.component(state)
//
//  override def refreshInterval = 10000
//}