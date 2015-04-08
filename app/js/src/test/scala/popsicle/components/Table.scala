package popsicle.components

import japgolly.scalajs.react._
import japgolly.scalajs.react.test.ReactTestUtils
import japgolly.scalajs.react.vdom.all._
import popsicle.components.bootstrap.Table
import utest._

object TableComponent extends TestSuite {

  val tests = TestSuite {
    import Table._

    def genRow: Map[String, () => TagMod] = Map(
      "col1" -> (() => "val1"),
      "col2" -> (() => "val2"),
      "col3" -> (() => "val3")
    )

    val tableData = TableData(
      List("col1", "col2", "col3"),
      List(genRow, genRow)
    )

    val tableComponent = new Table(tableData)

    'tableComponent {
      val c = ReactTestUtils.renderIntoDocument(tableComponent.renderable)
      val cols = ReactTestUtils.scryRenderedDOMComponentsWithTag(c, "th")
      val rows = ReactTestUtils.scryRenderedDOMComponentsWithTag(c, "tr")
      assert(cols.size == 3)
      assert(rows.size == 2)
    }
  }

}
