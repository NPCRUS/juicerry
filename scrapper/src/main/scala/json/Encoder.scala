package json

import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.{Encoder, Json}
import models.JsonSupport.ingredientTypeEncoder
import models.{IngredientType, Recipe}

object Encoder {

  def encodeRecipes(data: Seq[Recipe]): String =
    data.asJson.spaces2
    
  def encodeIngredients(data: Seq[String]): String =
    data.asJson.spaces2
}
