package popsicle.db

import reactivemongo.api._
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.ExecutionContext.Implicits.global

object Mongo {
  // The driver creates an actor system.
  private val driver = new MongoDriver

  // The connection is a connection pool of akka actors.
  private val connection = driver.connection(List("localhost"))

  // Databases
  def garbanzo = connection("garbanzo_development")
  def kale = connection("kale_development")

  // Collections
  def products: BSONCollection = garbanzo("products")
  def producers: BSONCollection = garbanzo("vendors")
  def orderItems: BSONCollection = garbanzo("order_item")
  def opsItems: BSONCollection = kale("ops_item")
}