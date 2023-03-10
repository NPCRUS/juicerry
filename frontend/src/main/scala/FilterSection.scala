import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.IngredientType.{AllYear, Berry, Exotic, Spice, Summer, Winter}
import models.{Ingredient, IngredientType}
import org.scalajs.dom.MouseEvent

object FilterSection {
  def render: HtmlElement =
    val filterNodesSignal = State.filtersSignal.map { filters =>
      filters.groupBy(_._1.typ)
        .toSeq
        .sortWith {
          case ((a, _), (b, _)) if a == IngredientType.AllYear => true
          case _ => false
        }
        .map(filterByBlock)
    }

    div(
      cls("flex flex-col bg-lime-200 p-2"),
      cls.toggle("hidden") <-- State.isFilterNodeHidden.signal,
      div(
        cls("flex flex-row gap-4"),
        children <-- filterNodesSignal
      ),
      div(
        cls("flex flex-row justify-end space-x-5"),
        button(
          cls("bg-white w-20"),
          onClick --> State.isFilterNodeHiddenUpdater,
          "close"
        ),
        button(
          cls("bg-white w-20"),
          onClick --> State.onSearchClick,
          "search"
        )
      )
    )

  def calculateBasis(ingredientType: IngredientType): String = ingredientType match
    case AllYear => "basis-2/4"
    case _ => "basis-1/6"

  def calculateGridCols(ingredientType: IngredientType): String = ingredientType match
    case AllYear => "grid-cols-2"
    case _ => "grid-cols-1"

  def filterByBlock(ingredientType: IngredientType,
                    ingredientsState: Seq[(Ingredient, Var[ThreeStateSwitch.State])]): HtmlElement =
    val mapped = Signal.fromValue(
      ingredientsState
        .map(t => ThreeStateSwitch.render(t._1.name, t._2))
    )

    div(
      cls("flex flex-col"),
      cls(calculateBasis(ingredientType)),
      span(s"${ingredientType.toString}:"),
      div(
        cls("grid gap-2"),
        cls(calculateGridCols(ingredientType)),
        children <-- mapped,
      )
    )
}
