import sttp.client3.httpclient.zio.HttpClientZioBackend
import zio.*
import zio.Console.*
import sttp.client3.*
import sttp.model.Uri

import scala.util.Try

object Main extends ZIOAppDefault {
  private val mainUrl = uri"https://www.nutrilovers.de/pages/slow-juice-saft-rezepte-sammlung"

  val appLogic = for {
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
          .flatMap(HtmlParser.parseRecipe)
          .logError(trueUrl.toString)
      }
    }

    jsonString <- ZIO.succeed(Encoder.encodeRecipes(recipes))
    _ <- ZIO.writeFile("./recipes.json", jsonString)
    _ <- printLine("file written successfully")
  } yield ()

  private def downloadHtml(uri: Uri): ZIO[SttpBackend[Task, Any], Throwable, String] =
    val request = basicRequest.get(uri)
    ZIO.service[SttpBackend[Task, Any]]
      .flatMap(backend => backend.send(request))
      .map(_.body.left.map(e => new Throwable(e)))
      .absolve


  override val run = appLogic.provideLayer(ZLayer(HttpClientZioBackend()))
}
