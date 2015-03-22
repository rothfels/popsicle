package models

case class Product(
  id: String,
  producer: String,
  name: String
)

case class Invoice(
  id: String,
  status: String,
  fulfillment: String
)