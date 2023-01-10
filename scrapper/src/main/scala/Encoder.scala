import io.circe.{Encoder, Json}
import io.circe.syntax.*
import io.circe.generic.auto.*
import models.{IngredientType, Recipe}
import models.JsonSupport.ingredientTypeEncoder

object Encoder {

  def encodeRecipes(data: Seq[Recipe]): String =
    data.asJson.spaces2
}
