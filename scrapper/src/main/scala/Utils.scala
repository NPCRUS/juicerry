import json.Decoder
import models.{Ingredient, JsonSupport}
import zio.ZIO

import sys.process.*
import java.io.IOException

object Utils {
  def loadMeasureUnits: ZIO[Any, IOException, Seq[String]] =
    ZIO.readFile("./scrapper/measure_units.json")
      .map(Decoder.decodeMeasureUnits)
      .flatMap {
        case Left(value) => ZIO.die(value)
        case Right(value) => ZIO.succeed(value)
      }

  def loadIngredients: ZIO[Any, IOException, Seq[Ingredient]] =
    ZIO.readFile("./scrapper/ingredients.json")
      .map(JsonSupport.decodeIngredients)
      .flatMap {
        case Left(value) => ZIO.die(value)
        case Right(value) => ZIO.succeed(value)
      }
    
  def calculateMostSimilar(input: String, strings: Seq[String]): String =
    Process(s"node ./compare.js \"$input\" \"${strings.mkString(",")}\"").lazyLines.head
}
