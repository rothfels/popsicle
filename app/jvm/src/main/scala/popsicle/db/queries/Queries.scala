package popsicle.db.queries

import popsicle.Queries

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

import reactivemongo.bson.BSONDocument

import popsicle.db.Mongo

trait MongoQueries extends Queries {
  import popsicle.db.collections.ProductCollection.ProductCollectionReader

  def getFlowduct(): models.Product = {
    val product = Mongo.products
      .find(BSONDocument())
      .one[models.Product]

    Await.result(product, 5.seconds).orNull
  }
}

//object Queries {
//
//
//
//  def main(args: Array[String]): Unit = {
//    val product = Mongo.products
//      .find(BSONDocument())
//      .one[Product]
//
//    product.onComplete {
//      case Success(product) => {
//        println("good")
//        println(product)
//      }
//      case Failure(t) => {
//        println("error" + t.getMessage)
//      }
//    }
////    product.foreach(product: Option[Product] => {
////      println(product)
////    })
//  }
//}
