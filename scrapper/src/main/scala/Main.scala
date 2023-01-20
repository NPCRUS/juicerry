import json.Encoder
import models.{Ingredient, Recipe}
import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.*
import zio.Console.*
import sttp.client3.*
import sttp.model.Uri

import scala.util.Try

object Main extends ZIOAppDefault {
  private val mainUrl = uri"https://www.nutrilovers.de/pages/slow-juice-saft-rezepte-sammlung"

  val appLogic = for {
    measureUnits <- Utils.loadMeasureUnits
    responseBody <- downloadHtml(mainUrl)

    fakeLinks <- HtmlParser.parseMain(responseBody).flatMap(links => ZIO.attempt(links.map(Uri.unsafeParse)))

    trueLinks <- ZIO.collectAllPar {
      fakeLinks.map { url =>
        val newUrl = url.host(mainUrl.host.getOrElse("")).scheme(mainUrl.scheme.getOrElse(""))
        downloadHtml(newUrl)
          .flatMap(HtmlParser.extractLink)
          .map(Uri.parse)
      }
    }.map(_.collect {
      case Right(url) if !url.toString.contains("page") => url
    })

    recipes <- ZIO.collectAllPar {
      trueLinks.map { trueUrl =>
        downloadHtml(trueUrl)
          .flatMap(HtmlParser.parseRecipe(_, measureUnits))
          .logError(trueUrl.toString)
      }
    }

    ingredients <- Utils.loadIngredients
    updatedRecipes <- replaceIngredients(recipes, ingredients)

    jsonString <- ZIO.succeed(Encoder.encodeRecipes(updatedRecipes))
    _ <- ZIO.writeFile("./recipes.json", jsonString)
    
//    _ <- ZIO.writeFile("./ingredients.json", Encoder.encodeIngredients(ingredients))
//    _ <- printLine("file written successfully")
  } yield ()

  private def downloadHtml(uri: Uri): ZIO[SttpBackend[Task, Any], Throwable, String] =
    val request = basicRequest.get(uri)
    ZIO.service[SttpBackend[Task, Any]]
      .flatMap(backend => backend.send(request))
      .map(_.body.left.map(e => new Throwable(e)))
      .absolve

  private def replaceIngredients(recipes: Seq[Recipe], ingredients: Seq[Ingredient]): ZIO[Any, Throwable, Seq[Recipe]] = ZIO.attempt {
    val existingIngredients = ingredients.map(_.name)

    recipes.map(recipe => recipe.copy(
      ingredients = recipe.ingredients.map { ia =>
        val similarityScore = Utils.calculateMostSimilar(ia.ingredient.name, existingIngredients)
        val found = ingredients.find(_.name == similarityScore.result).get
        val valid = similarityScore.score.compare(0.3) > 0

        val resultString = if (valid) {
          s"${found.name}(${found.id})"
        } else {
          s"not found ${similarityScore.result}"
        }

        println(s"${ia.ingredient.name} - $resultString (score: ${similarityScore.score})")
        val preparedIngredient = found.copy(name = ia.ingredient.name)

        if(valid)
          ia.copy(ingredient = preparedIngredient)
        else
          ia
      }
    ))
  }

  override val run = appLogic.provideLayer(ZLayer(HttpClientZioBackend()))
}
