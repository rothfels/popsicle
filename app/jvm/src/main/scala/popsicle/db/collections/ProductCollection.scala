package popsicle.db.collections

import org.joda.time.DateTime

import reactivemongo.bson._

//case class ProductCollection(
//  id: BSONObjectID,
//  producer: BSONObjectID,
//  name: String,
//  createdAt: DateTime
//)
//  content: String,
//  publisher: String,
//  creationDate: Option[DateTime],
//  updateDate: Option[DateTime]

object ProductCollection {
  implicit object ProductCollectionReader extends BSONDocumentReader[models.Product] {
    def read(doc: BSONDocument): models.Product = {
      val id = doc.getAs[BSONObjectID]("_id").get.toString
      val producer = doc.getAs[BSONObjectID]("vendor").get.toString
      val name = doc.getAs[String]("name").get
      models.Product(id, producer, name)
    }
  }
}