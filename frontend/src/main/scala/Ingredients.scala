import models.*

object Ingredients {
  val rawBeet = Ingredient(id = 1L, name = "Raw beet", typ = IngredientType.AllYear)
  val carrot = Ingredient(id = 2L, name = "Carrot", typ = IngredientType.AllYear)
  val orange = Ingredient(id = 3L, name = "Orange", typ = IngredientType.Exotic)
  val strawberries = Ingredient(id = 4l, name = "Strawberries", typ = IngredientType.Summer)
  val basil = Ingredient(id = 5l, name = "Basil", typ = IngredientType.AllYear)
  val pineapple = Ingredient(id = 6L, name = "Pineapple", typ = IngredientType.Exotic)
  val apple = Ingredient(id = 7L, name = "Apple", typ = IngredientType.AllYear)
  val mango = Ingredient(id = 8L, name = "Mango", typ = IngredientType.Exotic)
  val celery = Ingredient(id = 9L, name = "Celery", typ = IngredientType.AllYear)
  val ginger = Ingredient(id = 11L, name = "Ginger", typ = IngredientType.AllYear)
  val tumeric = Ingredient(id = 12L, name = "Tumeric", typ = IngredientType.Spice)
  val blackPepper = Ingredient(id = 13L, name = "Black pepper", typ = IngredientType.Spice)
  val coconutMilk = Ingredient(id = 14L, name = "Coconut milk", typ = IngredientType.Exotic)
  
  def all: Seq[Ingredient] = Seq(rawBeet, carrot, orange, strawberries, basil, pineapple, apple, mango, celery, ginger, tumeric, blackPepper, coconutMilk)
}
