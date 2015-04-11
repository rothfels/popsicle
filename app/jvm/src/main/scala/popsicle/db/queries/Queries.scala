package popsicle.db.queries

import popsicle.PopsicleRPC

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.bson.BSONDocument

import popsicle.db.Mongo

trait MongoQueryRPC /* extends PopsicleRPC */ {
  import popsicle.db.collections.ProductCollection.ProductCollectionReader

  def getProduct: Option[models.Product] = {
    val product = Mongo.products
      .find(BSONDocument())
      .one[models.Product]

    Await.result(product, 5.seconds)
  }
}