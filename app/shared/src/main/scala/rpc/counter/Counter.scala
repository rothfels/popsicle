package popsicle.rpc.counter

import popsicle.rpc

/**
 * Implemented on the server.
 */
trait CounterRPCServer extends rpc.PollRPC {
  def getCounter(): Int
}

/**
 * Implemented on the client.
 */
trait CounterRPCClient extends rpc.PushRPC {
  def incrementCounter(): Request
}
