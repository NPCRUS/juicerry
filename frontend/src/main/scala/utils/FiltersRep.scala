package utils

import components.ThreeStateSwitch

case class FiltersRep(ingredientId: Long,
                      state: ThreeStateSwitch.State)
