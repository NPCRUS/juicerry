import models.{Amount, IngredientAmount, Recipe}

object RecipesTestData {

  def apply(): Seq[Recipe] = Seq(
    Recipe(
      title = "RED BOMB for stomach & intestines",
      description = "This freshly squeezed juice is mild and gentle on the stomach. It provides you with valuable IngredientAmounts and promotes digestion. The IngredientAmounts also have a positive effect on your intestinal flora. The juice does not burden the organism and is a healthy and light drink with enormous added value.",
      pictureUrl = "https://cdn.shopify.com/s/files/1/0071/0587/1983/files/red_bomb_rote_beete__karotte_und_erdbeeren_f_r_eine_optimale_verdauung_nutrilovers_slow_juicer_saftrezepte_compr_4x3_49c1f6fc-504f-4201-8c3c-e18eab0ebbd3_480x480.jpeg?v=1597952894",
      ingredients = Seq(
        IngredientAmount(Ingredients.rawBeet, Amount(2)),
        IngredientAmount(Ingredients.carrot, Amount(2)),
        IngredientAmount(Ingredients.orange, Amount(1)),
        IngredientAmount(Ingredients.strawberries, Amount(BigDecimal(100), Some("gram"))),
        IngredientAmount(Ingredients.basil, Amount(BigDecimal(1), Some("bunch")))
      ),
      tags = Seq("digestion")
    ),
    Recipe(
      title = "Immune Booster - Exotic Golden Milk",
      description = "This juice is based on the traditional recipes for golden milk from Ayurvedic cuisine. You will love this exotic, distinctive and above all healthy juice.",
      pictureUrl = "https://cdn.shopify.com/s/files/1/0071/0587/1983/files/immun-booster_-_exotische_goldene_milch_nutrilovers_slow_juicer_saftrezepte_compr_4x3_9bbb09e0-e14a-44bb-bea7-5a09eb9d2a08_480x480.jpeg?v=1597912432",
      ingredients = Seq(
        IngredientAmount(Ingredients.pineapple, Amount(BigDecimal(0.25f))),
        IngredientAmount(Ingredients.apple, Amount(1)),
        IngredientAmount(Ingredients.mango, Amount(BigDecimal(0.5f))),
        IngredientAmount(Ingredients.celery, Amount(2)),
        IngredientAmount(Ingredients.carrot, Amount(1)),
        IngredientAmount(Ingredients.ginger, Amount(BigDecimal(0.5f), Some("inch"))),
        IngredientAmount(Ingredients.tumeric, Amount(BigDecimal(1), Some("cm"))),
        IngredientAmount(Ingredients.blackPepper, Amount(BigDecimal(1), Some("pinch"))),
        IngredientAmount(Ingredients.coconutMilk, Amount(BigDecimal(2), Some("tbsp")))
      ),
      tags = Seq("immunity")
    )
  )
}
