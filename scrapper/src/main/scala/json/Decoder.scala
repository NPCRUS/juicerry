package json

import io.circe
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.parser.decode
import models.Ingredient
import models.JsonSupport.ingredientTypeDecoder

object Decoder {
  def decodeMeasureUnits(str: String): Either[circe.Error, Seq[String]] =
    decode[Seq[String]](str)
}
