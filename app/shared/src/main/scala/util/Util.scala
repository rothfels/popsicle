package popsicle.util

object Util {
  // Get Map[String, Any] of CaseClass params to values.
  def getCCParams(cc: Product) = {
    val values = cc.productIterator
    cc.getClass.getDeclaredFields.map( _.getName -> values.next ).toMap
  }
}