package models

case class IngredientAmount(ingredient: Ingredient,
                            amount: Amount) {
  def toStringLabel: String = s"${ingredient.name} x ${amount.amount} ${amount.measureUnit.getOrElse("")}"
}
