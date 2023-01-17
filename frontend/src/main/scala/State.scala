import com.raquo.airstream.web.AjaxEventStream
import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.{Ingredient, JsonSupport, Recipe}
import org.scalajs.dom.{EventSource, MouseEvent}

object State {

  val isFilterNodeHidden: Var[Boolean] = Var(false)
  val isFilterNodeHiddenUpdater: Observer[MouseEvent] = State.isFilterNodeHidden.updater[MouseEvent]((v, _) => !v)

  val (onSearchStream, onSearchClick) = EventStream.withObserver[MouseEvent]

  private val recipesSignal: Signal[Seq[Recipe]] = AjaxEventStream.get(
    url = "./recipes.json"
  ).map(r => JsonSupport.decodeRecipes(r.responseText))
    .toSignal(Seq.empty)

  private val ingredientsSignal: Signal[Seq[Ingredient]] = AjaxEventStream.get(
    url = "./ingredients.json"
  ).map(r => JsonSupport.decodeIngredients(r.responseText).fold(_ => Seq.empty, r => r))
    .toSignal(Seq.empty)

  val filtersSignal: Signal[Seq[(Ingredient, Var[ThreeStateSwitch.State])]] =
    ingredientsSignal.map(_.map((_, Var(ThreeStateSwitch.State.Neutral))))


  val recipes: EventStream[Seq[Recipe]] = onSearchStream
    .withCurrentValueOf(recipesSignal, filtersSignal)
    .map {
      case (_, recipes, currentFilters) =>
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
          .map(_._1)
    }
}
