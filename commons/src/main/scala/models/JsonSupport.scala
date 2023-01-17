package models


import io.circe
import io.circe.Decoder.Result
import io.circe.parser.decode
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.auto.*

import scala.util.Try

object JsonSupport {
  implicit val ingredientTypeEncoder: Encoder[IngredientType] = (a: IngredientType) => Json.fromString(a.toString)
  implicit val ingredientTypeDecoder: Decoder[IngredientType] =
    Decoder.decodeString.emapTry(s => Try(IngredientType.valueOf(s)))

  def decodeRecipes(jsonString: String): Seq[Recipe] =
    decode[Seq[Recipe]](jsonString).fold(_ => Seq.empty, seq => seq)

  def decodeIngredients(str: String): Either[circe.Error, Seq[Ingredient]] =
    decode[Seq[Ingredient]](str)
}
