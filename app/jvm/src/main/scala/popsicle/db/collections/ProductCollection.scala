package popsicle.db.collections

import reactivemongo.bson._

object ProductCollection {
  implicit object ProductCollectionReader extends BSONDocumentReader[models.Product] {
    def read(doc: BSONDocument): models.Product = {
      val id = doc.getAs[BSONObjectID]("_id").get.stringify
      val producer = doc.getAs[BSONObjectID]("vendor").get.stringify
      val name = doc.getAs[String]("name").get
      models.Product(id, producer, name)
    }
  }
}