package game.cauldrons

import game.brewing.BrewingResult
import game.ingredients.Ingredient
import game.inventory.Inventory
import game.inventory.InventoryItem


sealed class Cauldron(displayNamePrefix: String) : InventoryItem {
    override val displayName = "$displayNamePrefix Cauldron"
    val ingredients = Inventory<Ingredient>()

    fun getBrewResult() =
        if (ingredients.isEmpty) null else BrewingResult(this)

    class Mudpack : Cauldron("Mudpack")
}
