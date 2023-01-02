import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import models.Ingredient

object HeaderSection {

  def render: HtmlElement =
    div(
      cls("flex flex-row bg-lime-100 p-2 space-x-4"),
      titleNode,
      searchBarNode
    )

  private val titleNode: Div = div(
    cls("flex bg-white rounded shrink"),
    span(
      cls("px-4 py-1"),
      "Juicerry"
    )
  )

  private val searchBarNode: Div = div(
    cls("flex flex-row bg-white grow rounded cursor-pointer"),
    onClick --> State.isFilterNodeHiddenUpdater,
    children <-- State.filters.map(_.map(filterTag))
  )

  private def filterTag(ingredient: Ingredient, state: Var[ThreeStateSwitch.State]): Div =
    val background = state.signal.map {
      case ThreeStateSwitch.State.Negative => "bg-red-200"
      case ThreeStateSwitch.State.Neutral => "bg-gray-200"
      case ThreeStateSwitch.State.Positive => "bg-green-200"
    }

    val hidden = state.signal.map(_ == ThreeStateSwitch.State.Neutral)

    div(
      cls("m-2"),
      cls.toggle("hidden") <-- hidden,
      cls <-- background,
      child <-- state.signal.map { state =>
        val stateText = state match
          case ThreeStateSwitch.State.Negative => "off"
          case ThreeStateSwitch.State.Positive => "on"
          case ThreeStateSwitch.State.Neutral => "not selected"

        span(s"${ingredient.name}: $stateText")
      }
    )
}
