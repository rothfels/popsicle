package popsicle.rpc

// Interface for client -> server requests (via ajax post).
trait PollRPC {}

// Interface for server -> client push messages (via websocket).
trait PushRPC {
  /**
   * Syntactic sugar only.
   * When the server makes a method call to the socket push rpc,
   * autowire requires a non-unit typed response.
   *
   * (The response here is represented as the autowire request
   * to be sent over the websocket).
   *
   * The implementing class is free to return null.
   */
  type Request = autowire.Core.Request[String]

  /**
   * Invoke WebSocketPushRPC method by pickle'd request.
   * This is the entry point for an auto-wired method RPC
   * delivered by server push over websocket.
   *
   * @param reqPickle pickled autowire Core.Request
   */
  def call(reqPickle: String): Unit
}