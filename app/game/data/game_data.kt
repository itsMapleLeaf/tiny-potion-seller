package game.data


data class GameData(
    var dayNumber: Int = 1,
    var timeRemaining: Int = 6,
    var gold: Int = 0,
    val ingredients: MutableMap<IngredientSpec, Int> = mutableMapOf(),
    val potions: MutableMap<Potion, Int> = mutableMapOf(),
)


data class IngredientSpec(
    val name: String,
    val magimins: MagiminCounts,
) {
    init {
        libraryInternal.add(this)
    }

    companion object {
        val library get(): List<IngredientSpec> = libraryInternal

        private val libraryInternal = mutableListOf<IngredientSpec>()

        val Feyberry = IngredientSpec("Feyberry", magiminCounts(a = 6))
        val MandrakeRoot = IngredientSpec("Mandrake Root", magiminCounts(b = 6))
        val SackOfSlime = IngredientSpec("Sack of Slime", magiminCounts(c = 6))
    }
}
