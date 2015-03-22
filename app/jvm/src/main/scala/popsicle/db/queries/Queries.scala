package popsicle.db.queries

import popsicle.RPC

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.bson.BSONDocument

import popsicle.db.Mongo

trait MongoQueryRPC extends RPC {
  import popsicle.db.collections.ProductCollection.ProductCollectionReader

  def getProduct(): models.Product = {
    val product = Mongo.products
      .find(BSONDocument())
      .one[models.Product]

    Await.result(product, 5.seconds).orNull
  }
}