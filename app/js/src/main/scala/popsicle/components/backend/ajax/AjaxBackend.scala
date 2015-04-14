package popsicle.components.backend.ajax

import popsicle.components.backend.ComponentBackend

import japgolly.scalajs.react.BackendScope

import scala.concurrent.Future
import scala.scalajs.js

abstract class AjaxBackend[State]($: BackendScope[_, State]) extends ComponentBackend($) {
  import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow

  // An async function which returns new state.
  def ajaxFn: Future[State]

  def setStateAsync(): Unit = {
    ajaxFn.foreach($.setState(_))
  }

  var interval: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined
  val refreshInterval = 0 // How often to poll ajaxFn in millis (0 => once when component mount)

  override def init(): Unit = {
    setStateAsync()
    if (refreshInterval > 0) {
      interval = js.timers.setInterval(refreshInterval)(setStateAsync())
    }
  }

  override def close(): Unit = {
    // Clear interval (if defined).
    interval.foreach(js.timers.clearInterval)
  }
}