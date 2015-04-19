package popsicle.components

import popsicle.components.backend.{IntervalAjaxBackend, AjaxBackend, AjaxComponent}

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}
import popsicle.components.util.TestStringBackendComponent

import utest._

import scala.scalajs.js
import scala.concurrent.{Promise, Future}
import scala.util.{Try, Success}

object AjaxComponentTest extends TestSuite {

  case class IncompleteFutureAjaxBackend($: BackendScope[_, String]) extends AjaxBackend($) {
    override def ajax: Future[String] = Promise().future
  }

  case class CompleteFutureAjaxBackend($: BackendScope[_, String]) extends AjaxBackend($) {
    import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
    override def ajax: Future[String] = Future("future")
  }

  val tests = TestSuite {
    'incompleteFutureShouldShowInitialState {
      val incompleteFuture = TestStringBackendComponent(IncompleteFutureAjaxBackend.apply)
      val component = ReactTestUtils.renderIntoDocument(incompleteFuture.buildComponent())
      TestStringBackendComponent.assertState(component, "initial")
    }

    'completeFutureShouldShowResolvedAjaxState {
      val completeFuture = TestStringBackendComponent(CompleteFutureAjaxBackend.apply)
      val component = ReactTestUtils.renderIntoDocument(completeFuture.buildComponent())
      TestStringBackendComponent.assertState(component, "future")
    }
  }
}

object IntervalAjaxComponentTest extends TestSuite {

  case class RefreshCounterBackend($: BackendScope[_, String]) extends IntervalAjaxBackend($) {
    var count = 0
    override def ajax: Future[String] = {
      // Incomplete first ajax call; complete second ajax call.
      val promise = Promise[String]()
      promise.complete(Success(s"${count}"))
      count += 1
      promise.future
    }
    override def refreshInterval = 1000
  }

  val tests = TestSuite {
    'shouldResolveAjaxStateOnInterval {
      val refreshCounter = TestStringBackendComponent(RefreshCounterBackend.apply)
      val component = ReactTestUtils.renderIntoDocument(refreshCounter.buildComponent())
      TestStringBackendComponent.assertState(component, "0")

      val promise = Promise[Unit]()
      js.timers.setTimeout(2500) {
        // Should call ajax twice before we execute the assertion.
        promise.complete(Try(TestStringBackendComponent.assertState(component, "2")))
      }

      promise.future
    }
  }

}