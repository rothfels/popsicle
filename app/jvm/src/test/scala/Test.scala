import utest._

object ServerTest extends TestSuite {

  // Initialize App
//  TutorialApp.setupUI()

  def tests = TestSuite {
    'HelloWorld {
      assert(Array(1).length == 1)
    }
  }
}
