package serde

import io.circe.Decoder.{Result, decodeString}
import io.circe.{Decoder, Encoder, HCursor, Json}
import models.IngredientType

import scala.util.Try

object IngredientTypeSerde {

  given Encoder[IngredientType] = Encoder.encodeString.contramap(v => v.toString)

  given Decoder[IngredientType] = Decoder.decodeString.emapTry(s => Try(IngredientType.valueOf(s)))

}
