import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.{Ingredient, IngredientType}
import org.scalajs.dom.MouseEvent

object FilterSection {
  def render: HtmlElement =
    val filterNodesSignal = State.filters.map { filters =>
      filters.groupBy(_._1.typ)
        .collect {
          case (ingredientType, seq) if seq.nonEmpty => (ingredientType, seq)
        }
        .toSeq
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

  def filterByBlock(ingredientType: IngredientType,
                    ingredientsState: Seq[(Ingredient, Var[ThreeStateSwitch.State])]): HtmlElement =
    val mapped = Signal.fromValue(
      ingredientsState
        .map(t => ThreeStateSwitch.render(t._1.name, t._2))
    )

    div(
      cls("flex flex-col basis-1/4"),
      span(s"${ingredientType.toString}:"),
      div(
        cls("grid grid-cols-2 gap-2"),
        children <-- mapped,
      )
    )
}
