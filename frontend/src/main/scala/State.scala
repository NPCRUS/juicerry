import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.{Ingredient, Recipe}
import org.scalajs.dom.{EventSource, MouseEvent}

object State {

  val isFilterNodeHidden: Var[Boolean] = Var(false)
  val isFilterNodeHiddenUpdater: Observer[MouseEvent] = State.isFilterNodeHidden.updater[MouseEvent]((v, _) => !v)

  val (onSearchStream, onSearchClick) = EventStream.withObserver[MouseEvent]

  val filters: Val[Seq[(Ingredient, Var[ThreeStateSwitch.State])]] = Val(
    Ingredients.all.map((_, Var(ThreeStateSwitch.State.Neutral)))
  )

  val recipes = onSearchStream.map { _ =>
    val currentFilters = filters.now()
    val allowedIngredients = currentFilters.filter(_._2.now() == ThreeStateSwitch.State.Positive)
    val bannedIngredients = currentFilters.filter(_._2.now() == ThreeStateSwitch.State.Negative).map(_._1)
    
    RecipesTestData.apply()
      .filter(r => r.ingredients.map(_.ingredient).intersect(bannedIngredients).isEmpty)
  }
}
