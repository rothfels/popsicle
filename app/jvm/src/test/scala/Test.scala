import utest._

object TutorialTest extends TestSuite {

  // Initialize App
//  TutorialApp.setupUI()

  def tests = TestSuite {
    'HelloWorld {
      assert(Array(1).length == 1)
    }
  }
}
