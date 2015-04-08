package popsicle.components.bootstrap

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.all._

object Table {

  sealed abstract class TableType(val classString: String)
  case object TableStriped extends TableType("table-striped")
  case object TableBordered extends TableType("table-bordered")
  case object TableHover extends TableType("table-hover")
  case object TableCondensed extends TableType("table-condensed")

  case class TableClass(tableTypes: List[TableType]) {
    def classString = "table " + tableTypes.map(_.classString).mkString(" ")
  }

  case class TableData(
    columns: List[String],
    rows: List[Map[String, () => TagMod]],
    tableClass: TableClass = TableClass(List(TableStriped, TableBordered, TableHover))
  )
}

import popsicle.components.bootstrap.Table._

class Table(data: TableData) {
  val renderable = component(data)

  def component = ReactComponentB[TableData]("table")
    .render($ => {
      table(`class` := $.tableClass.classString,
        thead(
          $.columns.map(th(_))
        ),
        tbody(
          $.rows.map(row => {
            tr(
              $.columns.map(field => {
                row.get(field) match {
                  case Some(component) => td(component())
                  case None => td("n/a")
                }
              })
            )
          })
        )
      )
    })
    .build
}