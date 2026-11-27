package game.data

import kotlinx.serialization.Serializable

@Serializable
enum class CauldronSpec(
    val maxIngredientCount: Int,
    val maxMagiminCount: Int,
) : InventoryItem {
    Wooden(maxIngredientCount = 4, maxMagiminCount = 75),
    Clay(maxIngredientCount = 6, maxMagiminCount = 115),
    Mudpack(maxIngredientCount = 6, maxMagiminCount = 160),
    ;

    override val displayName = "${javaClass.name} Cauldron"
}

@Serializable
class CauldronState(val cauldron: CauldronSpec) {
    val ingredients = Inventory<Ingredient>()

    fun getBrewResult() =
        if (ingredients.isEmpty) null else BrewingResult(this)
}
