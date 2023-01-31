package models

case class Recipe(title: String,
                  description: String,
                  pictureUrl: String,
                  ingredients: Seq[IngredientAmount],
                  tags: Seq[String])
