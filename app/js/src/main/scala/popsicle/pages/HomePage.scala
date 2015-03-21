package popsicle.pages

import japgolly.scalajs.react._, vdom.all._

object HomePage {

  val content = div(
    h1(
      a(
        color := "#000",
        href := "https://github.com/goodeggs/popsicle",
        "have a popsicle!"
      )
    ),
    section(marginTop := "1.6em", fontSize := "115%", color := "#444",
      p("Lifts Facebook's ",
        a(href := "http://facebook.github.io/react", "React"),
        " library into ",
        a(href := "http://www.scala-js.org", "Scala.js"),
        " and endeavours to make it as type-safe and Scala-friendly as possible."
      ),
      p("In addition to wrapping React, this provides extra functionality to support server RPC from React components.")
    ),
    p(marginTop := "3em", fontStyle := "italic",
      "Big thanks to ",
      a(href := "https://github.com/lihaoyi", "lihaoyi"),
      " for creating some awesome tools."
    )
  )
}
