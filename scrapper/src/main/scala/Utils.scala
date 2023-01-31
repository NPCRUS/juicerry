import models.Ingredient
import zio.ZIO
import io.circe.parser.decode
import io.circe.generic.auto.*
import serde.IngredientTypeSerde.given

import sys.process.*
import java.io.IOException

object Utils {
  def loadMeasureUnits: ZIO[Any, IOException, Seq[String]] =
    ZIO.readFile("./scrapper/measure_units.json")
      .map(decode[Seq[String]])
      .flatMap {
        case Left(value) => ZIO.die(value)
        case Right(value) =>
          ZIO.succeed(value)
      }

  def loadIngredients: ZIO[Any, IOException, Seq[Ingredient]] =
    ZIO.readFile("./scrapper/ingredients.json")
      .map(decode[Seq[Ingredient]])
      .flatMap {
        case Left(value) => ZIO.die(value)
        case Right(value) => ZIO.succeed(value)
      }
    
  def calculateMostSimilar(input: String, strings: Seq[String]): SimilarityScore =
    val resultStr = Process(s"node ./compare.js \"$input\" \"${strings.mkString(",")}\"").lazyLines.head
    resultStr.split(',').toList match
      case result :: scoreStr :: Nil => SimilarityScore(result, BigDecimal(scoreStr))
      case str => throw new Exception(s"Wrong similarity calculation result format: $str")
}
