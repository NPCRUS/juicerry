import models.{Amount, Ingredient, IngredientAmount, IngredientType, Recipe}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.*
import net.ruippeixotog.scalascraper.dsl.DSL.Extract.*
import net.ruippeixotog.scalascraper.dsl.DSL.Parse.*
import sttp.model.Uri
import zio.ZIO

import scala.util.Try

object HtmlParser {

  lazy private val browser = JsoupBrowser()

  def parseMain(htmlText: String): ZIO[Any, Throwable, Seq[String]] = ZIO.attempt {
    val document = browser.parseString(htmlText)
    val links = document >> elementList(".recipe-info a")
    links.map(_.attr("href"))
  }

  def extractLink(htmlText: String): ZIO[Any, Throwable, String] = ZIO.attempt {
    val document = browser.parseString(htmlText)
    val link = document >> elementList(".page_content a")

    link.map(_.attr("href")).find(_.contains("blog")) match
      case Some(value) => value
      case None => throw new Exception(s"cannot find blog link for ${document.title}")
  }

  def parseRecipe(htmlText: String, measureUnits: Seq[String]): ZIO[Any, Throwable, Recipe] =
    ZIO.attempt {
      val document = browser.parseString (htmlText)

      val title = (document >> element (".rk_heading")).text
      val description = (document >> element (".content-blog-detail span")).text
      val image = (document >> element (".blog-image img")).attr ("src")

      val ingredients = (document >> elementList (".rk_ingredients li"))
      .map (_.text)
      .map(parseIngredient(_, measureUnits))
      .collect {
        case Some (v) => v
      }

      Recipe(title, description, image, ingredients, Seq.empty)
    }

  private def parseIngredient(text: String, measureUnits: Seq[String]): Option[IngredientAmount] =
    val _isMeasureUnit = isMeasureUnit(measureUnits)
    val sections = removeDirt(text).split(" ").toList.map(_.trim)
    sections match
      case amount :: ingredient :: Nil if isAmount(amount) =>
        Some(IngredientAmount(Ingredient(0L, ingredient, IngredientType.AllYear), Amount(BigDecimal(amount), None)))
      case amount :: measureUnit :: rest if _isMeasureUnit(measureUnit) && isAmount(amount) =>
        Some(IngredientAmount(Ingredient(0L, rest.mkString(" "), IngredientType.AllYear), Amount(BigDecimal(amount), Some(measureUnit))))
      case amount :: rest if isAmount(amount) =>
        Some(IngredientAmount(Ingredient(0L, rest.mkString(" "), IngredientType.AllYear), Amount(BigDecimal(amount))))
      case _ =>
        None

  private def isAmount(amount: String): Boolean =
    Try(BigDecimal(amount)).isSuccess

  private def isMeasureUnit(units: Seq[String])(str: String): Boolean =
    units.map(_.toLowerCase).contains(str.toLowerCase)

  private def removeDirt(str: String): String =
    str.replace("voll ", "")
      .replace("vom ", "")
}
