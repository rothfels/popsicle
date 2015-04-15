package popsicle.rpc.counter

import popsicle.rpc.PushRPCClient
import rx._

object Counter {
  val rxCounter = Var(5)

  trait Server extends CounterRPCServer {
    def getCounter = Counter.rxCounter()
  }

  object Client {
    import scala.concurrent.ExecutionContext.Implicits.global
    def incrementCounter = PushRPCClient[CounterRPCClient].incrementCounter.call()
  }
}
