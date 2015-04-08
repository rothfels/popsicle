package popsicle.components

import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}
import popsicle.components.backend.AjaxComponent
import utest._

import scala.concurrent.{Promise, Future}
import scala.scalajs.js
import scala.util.{Try, Success}


object AjaxComponentTest extends TestSuite {

  abstract class TestAjaxComponent extends AjaxComponent[String] {
    override def initialState = AjaxComponent.State("initial")

    import japgolly.scalajs.react._, vdom.all._
    override def stateComponent(state: String) = div(`class` := "state", state)
  }

  def assertState(c: ComponentM, state: String): Unit = {
    val el = ReactTestUtils.findRenderedDOMComponentWithClass(c, "state")
    assert(el.getDOMNode().innerHTML == state)
  }

  val tests = TestSuite {

    'nonRefreshingAjax {

      'incompleteFuture {
        class IncompleteComponent extends TestAjaxComponent {
          override def refreshState: Future[String] = Promise().future
        }

        val c = ReactTestUtils.renderIntoDocument(new IncompleteComponent().component())
        assertState(c, "initial")
      }

      'completeFuture {
        class CompletedComponent extends TestAjaxComponent {
          import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
          override def refreshState: Future[String] = Future("future")
        }

        val c = ReactTestUtils.renderIntoDocument(new CompletedComponent().component())
        assertState(c, "future")
      }
    }

    'refreshingAjax {
      class RefreshAjaxComponent extends TestAjaxComponent {
        var refreshCount = 0
        override def refreshState: Future[String] = {
          val promise = Promise[String]()
          if (refreshCount > 0) {
            // complete only after first call to refresh
            promise.complete(Success("future"))
          }
          refreshCount += 1
          promise.future
        }
        override def refreshInterval = 1000
      }

      'refreshDelay {
        val c = ReactTestUtils.renderIntoDocument(new RefreshAjaxComponent().component())
        assertState(c, "initial")

        val promise = Promise[Unit]()
        js.timers.setTimeout(2000) {
          promise.complete(Try(assertState(c, "future")))
        }

        promise.future
      }
    }
  }

}
