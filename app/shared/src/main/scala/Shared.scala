package popsicle

trait PopsicleRPC {
  def getProduct(): Option[models.Product]
}