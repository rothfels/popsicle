import org.scalajs.dom.raw.HTMLInputElement
import utest._
import japgolly.scalajs.react._
import vdom.all._
import japgolly.scalajs.react.test._

object ClientTest extends TestSuite {

  // Initialize App
  //  TutorialApp.setupUI()

  def tests = TestSuite {
    'HelloWorld {
      assert(Array(1).length == 1)
    }
  }
}

object TestTest extends TestSuite {

  lazy val A = ReactComponentB[Unit]("A").render((_,c) => p(cls := "AA", c)).buildU
  lazy val B = ReactComponentB[Unit]("B").render(_ => p(cls := "BB", "hehehe")).buildU
  lazy val rab = ReactTestUtils.renderIntoDocument(A(B()))

  val inputRef = Ref[HTMLInputElement]("r")
  lazy val IC = ReactComponentB[Unit]("IC").initialState(true).renderS((t,_,s) => {
    val ch = (e: ReactEvent) => t.modState(x => !x)
    label(
      input(`type` := "checkbox", checked := s, onClick ==> ch, ref := inputRef),
      span(s"s = $s")
    )
  }).buildU

  lazy val IT = ReactComponentB[Unit]("IT").initialState("NIL").renderS((t,_,s) => {
    val ch = (e: SyntheticEvent[HTMLInputElement]) => t.setState(e.target.value.toUpperCase)
    input(`type` := "text", value := s, onChange ==> ch)
  }).buildU

  val tests = TestSuite {
    'isTextComponent {
      val r = ReactTestUtils.isTextComponent(A())
      assert(!r)
    }

    'findRenderedDOMComponentWithClass {
      val n = ReactTestUtils.findRenderedDOMComponentWithClass(rab, "BB").getDOMNode()
      assert(n.className == "BB")
    }

    'findRenderedComponentWithType {
      val n = ReactTestUtils.findRenderedComponentWithType(rab, B).getDOMNode()
      assert(n.className == "BB")
    }

//    'renderIntoDocument {
//      def test(c: ComponentM, exp: String): Unit = {
//        val h = removeReactDataAttr(c.getDOMNode().outerHTML)
//        h mustEqual exp
//      }
//      'plainElement {
//        val re: ReactElement = div("Good")
//        val c = ReactTestUtils.renderIntoDocument(re)
//        test(c, """<div>Good</div>""")
//      }
//      'component {
//        val c: ReactComponentM[Unit, Unit, Unit, TopNode] = ReactTestUtils.renderIntoDocument(B())
//        test(c, """<p class="BB">hehehe</p>""")
//      }
//    }

    'Simulate {
      'click {
        val c = ReactTestUtils.renderIntoDocument(IC())
        val i = inputRef(c).get
        val s = ReactTestUtils.findRenderedDOMComponentWithTag(c, "span")
        val a = s.getDOMNode().innerHTML
        ReactTestUtils.Simulate.click(i)
        val b = s.getDOMNode().innerHTML
        assert(a != b)
      }
      'change {
        val c = ReactTestUtils.renderIntoDocument(IT()).domType[HTMLInputElement]
        ChangeEventData("hehe").simulate(c)
        val t = c.getDOMNode().value
        assert(t == "HEHE")
      }
      'focusChangeBlur {
        var events = Vector.empty[String]
        val C = ReactComponentB[Unit]("C").initialState("ey").render(T => {
          def e(s: String): Unit = events :+= s
          def chg: ReactEventI => Unit = ev => {
            e("change")
            T.setState(ev.target.value)
          }
          input(value := T.state, ref := inputRef, onFocus --> e("focus"), onChange ==> chg, onBlur --> e("blur"))
        }).buildU
        val c = ReactTestUtils.renderIntoDocument(C())
        val i = inputRef(c).get
        Simulation.focusChangeBlur("good") run i
        assert(events == Vector("focus", "change", "blur"))
        assert(i.getDOMNode().value == "good")
      }
      'targetByName {
        val c = ReactTestUtils.renderIntoDocument(IC())
        var count = 0
        def tgt = {
          count += 1
          Sel("input").findIn(c)
        }
        Simulation.focusChangeBlur("-") run tgt
        assert(count == 3)
      }
    }
  }
}
