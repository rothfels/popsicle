package popsicle.components

import popsicle.components.backend.ajax.AjaxBackend

import japgolly.scalajs.react.BackendScope
import japgolly.scalajs.react.test.{ComponentM, ReactTestUtils}

import utest._

import scala.scalajs.js
import scala.concurrent.{Promise, Future}
import scala.util.{Try, Success}


object AjaxComponentTest extends TestSuite {

  class TestAjaxComponent(backendFactory: BackendScope[_, String] => AjaxBackend[String])
    extends AjaxComponent[String](backendFactory) {

    import japgolly.scalajs.react._, vdom.all._

    override def initState = "initial"
    override def renderState(state: String) = div(`class` := "state", state)
  }

  def assertState(component: ComponentM, state: String): Unit = {
    val el = ReactTestUtils.findRenderedDOMComponentWithClass(component, "state")
    assert(el.getDOMNode().innerHTML == state)
  }

  val tests = TestSuite {
    'nonRefreshingAjax {

      'incompleteFuture {
        case class IncompleteFuture($: BackendScope[_, String]) extends AjaxBackend($) {
          override def ajaxFn: Future[String] = Promise().future
        }

        val incompleteFuture = new TestAjaxComponent(IncompleteFuture.apply)
        val component = ReactTestUtils.renderIntoDocument(incompleteFuture.buildComponent())
        assertState(component, "initial")
      }

      'completeFuture {
        case class CompleteFuture($: BackendScope[_, String]) extends AjaxBackend($) {
          import scala.scalajs.concurrent.JSExecutionContext.Implicits.runNow
          override def ajaxFn: Future[String] = Future("future")
        }

        val completeFuture = new TestAjaxComponent(CompleteFuture.apply)
        val component = ReactTestUtils.renderIntoDocument(completeFuture.buildComponent())
        assertState(component, "future")
      }
    }

    'refreshingAjax {
      case class RefreshAjax($: BackendScope[_, String]) extends AjaxBackend($) {
        var ajaxCount = 0
        override def ajaxFn: Future[String] = {
          // Incomplete first ajax call; complete second ajax call.
          val promise = Promise[String]()
          if (ajaxCount > 0) {
            promise.complete(Success("future"))
          }
          ajaxCount += 1
          promise.future
        }
        override val refreshInterval = 1000
      }

      'refreshDelay {
        val refreshAjax = new TestAjaxComponent(RefreshAjax.apply)
        val component = ReactTestUtils.renderIntoDocument(refreshAjax.buildComponent())
        assertState(component, "initial")

        val promise = Promise[Unit]()
        js.timers.setTimeout(2000) {
          promise.complete(Try(assertState(component, "future")))
        }

        promise.future
      }
    }
  }

}
