package popsicle.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.test.ReactTestUtils
import japgolly.scalajs.react.vdom.all._
import popsicle.components.bootstrap.Nav
import utest._

object NavComponentTest extends TestSuite {

  val tests = TestSuite {

    'Simulate {
      import Nav._

      val data = NavData(
        List(
          NavState("foo", () => "foo"),
          NavState("baz", () => "baz"),
          NavState("bar", () => "bar")
        ),
        NavProps(NavTabs)
      )

      val nav = new Nav(data)

      'click {
        val component = ReactTestUtils.renderIntoDocument(nav.nav)
//        val ref = nav._navRefs(1)(component).get
        val tabs = ReactTestUtils.scryRenderedDOMComponentsWithTag(component, "li")
        val el = ReactTestUtils.findRenderedDOMComponentWithClass(component, "panel-body")
        val a = el.getDOMNode().innerHTML
        println(a)
        ReactTestUtils.Simulate.click(tabs(1))
        val b = el.getDOMNode().innerHTML
        println(b)
        assert(a != b)
      }
    }
  }

}
