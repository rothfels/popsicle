package popsicle

// Interface for client -> server requests via ajax.
trait AjaxRPC {
  // def getProduct(): Option[models.Product]
  def getCounter(): Int
}

// Interface for server -> client push messages via websocket.
trait WebSocketPushRPC {
  /**
   * This type is for syntactic sugar only.
   * When the server makes a method call to the socket push rpc,
   * autowire requires a non-unit, typed response.
   *
   * (The response is represented as the autowire request sent
   * over the websocket pipe).
   *
   * The implementing class is free to return null.
   */
  type PushResponse = autowire.Core.Request[String]

  /**
   * Invoke WebSocketPushRPC method by pickle'd request.
   * This is the entry point for an auto-wired method RPC
   * delivered by server push over websocket.
   *
   * @param reqPickle pickled autowire Core.Request
   */
  def call(reqPickle: String): Unit

  def incrementCounter(): PushResponse
}