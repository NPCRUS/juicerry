import com.raquo.laminar.api.L.*
import components.ThreeStateSwitch
import org.scalajs.dom
import org.scalajs.dom.{Event, EventSource, MouseEvent}
import models.{Ingredient, IngredientType, Recipe}

object Main {

  val rootNode: Div = div(
    cls("flex flex-col"),
    HeaderSection.render,
    FilterSection.render,
    RecipesSection.render
  )

  def main(args: Array[String]): Unit =
    render(dom.document.body, rootNode)

}
