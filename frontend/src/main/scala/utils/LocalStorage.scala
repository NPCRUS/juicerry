package utils

import components.ThreeStateSwitch
import io.circe.{Decoder, Encoder, Json}
import io.circe.parser.decode
import io.circe.generic.auto.*
import io.circe.syntax._
import models.{Ingredient, IngredientType}
import org.scalajs.dom
import com.raquo.laminar.api.L.*
import serde.ThreeStateSwitchSerde.given

import scala.util.Try

object LocalStorage {
  private val key = "filters"

  def readFilters: Seq[FiltersRep] =
    val jsonString = dom.window.localStorage.getItem(key)
    decode[Seq[FiltersRep]](jsonString).getOrElse(Seq.empty)

  def writeFilters(signal: Seq[(Ingredient, Var[ThreeStateSwitch.State])]): Unit =
    dom.window.localStorage.setItem(
      key,
      signal.map(t => FiltersRep(t._1.id, t._2.now()))
        .filter(_.state != ThreeStateSwitch.State.Neutral)
        .asJson.noSpaces
    )
}
