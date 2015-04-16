package popsicle.components.backend

import japgolly.scalajs.react.BackendScope

import scala.concurrent.Future

abstract class AjaxBackend[State]($: BackendScope[_, State]) extends Backend($) {
  import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

  // An async function which returns state.
  def ajax: Future[State]

  def setStateAsync(): Unit = {
    ajax.foreach($.setState(_))
  }

  override def init(): Unit = {
    setStateAsync()
  }

  override def close(): Unit = {}
}

abstract class AjaxComponent[State]
  (backendFactory: BackendScope[_, State] => AjaxBackend[State]) extends BackendComponent(backendFactory) {
  override def componentName = "ajax-component"
}