import com.raquo.airstream.web.AjaxEventStream
import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.{Ingredient, Recipe}
import org.scalajs.dom.{EventSource, MouseEvent, document}
import utils.LocalStorage
import io.circe.generic.auto.*
import io.circe.parser.decode
import serde.IngredientTypeSerde.given

import scala.scalajs.js

object State {

  val isFilterNodeHidden: Var[Boolean] = Var(false)
  val isFilterNodeHiddenUpdater: Observer[MouseEvent] = State.isFilterNodeHidden.updater[MouseEvent]((v, _) => !v)

  val (onSearchStream, onSearchClick) = EventStream.withObserver[MouseEvent]

  // TODO: declare scalajs interface instead of using eval
  val onCopyClick: Observer[Recipe] = Observer[Recipe].apply(recipe => {
    val sep = "\\n"
    val recipeStr = recipe.ingredients
      .map(ia => s"${ia.ingredient.name} x ${ia.amount.amount.toString()} ${ia.amount.measureUnit.getOrElse("")}$sep")
      .foldLeft(s"${recipe.title}$sep")(_.concat(_))
    println()
    js.eval(s"navigator.clipboard.writeText('$recipeStr')")
  })

  private val recipesSignal: Signal[Seq[Recipe]] = AjaxEventStream.get(
    url = "./recipes.json"
  ).map(r => decode[Seq[Recipe]](r.responseText).getOrElse(Seq.empty))
    .toSignal(Seq.empty)

  private val ingredientsSignal: Signal[Seq[Ingredient]] = AjaxEventStream.get(
    url = "./ingredients.json"
  ).map(r => decode[Seq[Ingredient]](r.responseText).fold(_ => Seq.empty, r => r))
    .toSignal(Seq.empty)

  val filtersSignal: Signal[Seq[(Ingredient, Var[ThreeStateSwitch.State])]] =
    val initialFilters = LocalStorage.readFilters
    ingredientsSignal.map(_.map { ingredient =>
      val foundState = initialFilters
        .find(_.ingredientId == ingredient.id)
        .map(_.state)
        .getOrElse(ThreeStateSwitch.State.Neutral)
      (ingredient, Var.apply(foundState))
    })

  val recipes: EventStream[Seq[Recipe]] = onSearchStream
    .withCurrentValueOf(recipesSignal, filtersSignal)
    .map {
      case (_, recipes, currentFilters) =>
        // side effect
        LocalStorage.writeFilters(currentFilters)

        val bannedIngredientIds = currentFilters.filter {
          case (_, state) => state.now() == ThreeStateSwitch.State.Negative
        }.map(_._1.id)
        val includedIngredientIds = currentFilters.filter {
          case (_, state) => state.now() == ThreeStateSwitch.State.Positive
        }.map(_._1.id)

        recipes
          .filter(r => r.ingredients.map(_.ingredient.id).intersect(bannedIngredientIds).isEmpty)
          .map(r => (r, r.ingredients.map(_.ingredient.id).intersect(includedIngredientIds).length))
          .sortBy(_._2)(Ordering.Int.reverse)
          .collect {
            case(recipe, includedOverlap) if includedOverlap > 0 => recipe
          }
    }
}
