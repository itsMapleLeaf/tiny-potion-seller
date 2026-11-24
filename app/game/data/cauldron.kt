package game.data


sealed class Cauldron(displayNamePrefix: String) : InventoryItem {
    override val displayName = "$displayNamePrefix Cauldron"
    val ingredients = Inventory<Ingredient>()

    fun getBrewResult() =
        if (ingredients.isEmpty) null else BrewingResult(this)

    class Mudpack : Cauldron("Mudpack")
}
