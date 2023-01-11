import com.raquo.laminar.api.L.*
import models.Recipe

object RecipesSection {

  def render: HtmlElement =
    div(
      cls("flex flex-col p-2 gap-4"),
      children <-- State.recipes.map(_.map(renderRecipeNode))
    )

  def label(_cls: String)(text: String): Div = div(
    cls(_cls),
    cls("rounded"),
    span(
      cls("px-4 py-1"),
      text
    )
  )

  def renderRecipeNode(recipe: Recipe): Div = div(
    cls("flex flex-row gap-2"),
    div(
      cls("basis-1/4"),
      img(
        cls("rounded object-contain"),
        src(recipe.pictureUrl)
      )
    ),
    div(
      cls("flex flex-col basis-3/4 justify-between"),
      div(
        cls("flex flex-col gap-2"),
        h2(
          cls("text-lg"),
          recipe.title
        ),
        p(recipe.description),
        div(
          cls("flex flex-row flex-wrap gap-1"),
          children <-- EventStream.fromValue(recipe.ingredients).map(_.map(_.toStringLabel).map(label("bg-lime-500")))
        ),
        div(
          cls("flex flex-row flex-wrap gap-1"),
          children <-- EventStream.fromValue(recipe.tags).map(_.map(label("bg-green-500")))
        ),
      ),
      div(
        cls("flex justify-end"),
        label("bg-gray-200")("Votes: 15")
      )
  ),
  )
}
