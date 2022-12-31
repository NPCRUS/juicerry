package models

object Amount:
  def apply(amount: Int): Amount = Amount(BigDecimal(amount), None)

  def apply(amount: BigDecimal): Amount = Amount(amount, None)

case class Amount(amount: BigDecimal,
                  measureUnit: Option[String])
