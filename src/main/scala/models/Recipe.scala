package models

case class Recipe(title: String,
                  description: String,
                  pictureUrl: String,
                  ingredients: Seq[IngredientAmount],
                  tags: Seq[String])

case class IngredientAmount(ingredient: Ingredient,
                            amount: Amount) {
  def toStringLabel: String = s"${ingredient.name} x ${amount.amount} ${amount.measureUnit.getOrElse("")}"
}
