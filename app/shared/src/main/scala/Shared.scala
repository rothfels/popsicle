package popsicle



trait RPC {
  def getProduct(): Option[models.Product]
}