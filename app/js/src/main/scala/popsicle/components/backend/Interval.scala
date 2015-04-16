package popsicle.components.backend

import japgolly.scalajs.react.BackendScope

import scala.scalajs.js

trait Interval {

  var interval: js.UndefOr[js.timers.SetIntervalHandle] = js.undefined

  def runInterval(fn: () => Unit, millis: Int): Unit = {
    if (interval == js.undefined) closeInterval()
    interval = js.timers.setInterval(millis)(fn())
  }

  def closeInterval(): Unit = {
    interval.foreach(js.timers.clearInterval)
    interval = js.undefined
  }
}

/**
* Ajax backend which refreshes state on an interval.
*/
abstract class IntervalAjaxBackend[State]($: BackendScope[_, State]) extends AjaxBackend($) with Interval {

  def refreshInterval: Int

  override def init(): Unit = {
    super.init()

    if (refreshInterval > 0) {
      runInterval(setStateAsync, refreshInterval)
    }
  }

  override def close(): Unit = {
    super.close()

    closeInterval()
  }
}