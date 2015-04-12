package popsicle

/**
 * Interface for client -> server requests via ajax.
 */
trait AjaxRPC {
  // def getProduct(): Option[models.Product]
  def getCounter(): Int
}

/**
 * Interface for server -> client push messages via websocket.
 */
trait WebSocketPushRPC {
  type Push = autowire.Core.Request[String]

  def incrementCounter(name: String): Push
}