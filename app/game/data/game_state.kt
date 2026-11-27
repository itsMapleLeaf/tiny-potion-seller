package game.data

import kotlinx.serialization.Serializable

@Serializable
class GameState {
    var dayNumber = 1
    var timeRemaining = 6
    var gold = 300

    val ingredients = Inventory<Ingredient>()
    val potions = Inventory<Potion>()
    val cauldrons = Inventory<CauldronSpec>()

    var cauldron = CauldronState(CauldronSpec.Wooden)

    companion object {
        fun init() = GameState().apply {
            ingredients.add(Ingredient.Feyberry, 10)
            ingredients.add(Ingredient.MandrakeRoot, 10)
            ingredients.add(Ingredient.SackOfSlime, 10)
        }

    }
}
