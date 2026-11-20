package game


data class GameData(
    var gold: Int = 0,
    val ingredients: MutableMap<IngredientSpec, Int> = mutableMapOf(),
    val potions: MutableMap<PotionSpec, Int> = mutableMapOf(),
)

data class IngredientSpec(
    val name: String,
    val magimins: Magimins,
) {
    init {
        libraryInternal.add(this)
    }

    companion object {
        val library get(): List<IngredientSpec> = libraryInternal

        private val libraryInternal = mutableListOf<IngredientSpec>()

        val Feyberry = IngredientSpec("Feyberry", Magimins(a = 6))
        val MandrakeRoot = IngredientSpec("Mandrake Root", Magimins(b = 6))
        val SackOfSlime = IngredientSpec("Sack of Slime", Magimins(c = 6))
    }
}

data class Magimins(
    val a: Int = 0,
    val b: Int = 0,
    val c: Int = 0,
    val d: Int = 0,
    val e: Int = 0,
) {
    val displayText
        get() = mapOf("A" to a, "B" to b, "C" to c, "D" to d, "E" to e)
            .filter { (_, count) -> count > 0 }
            .map { (letter, count) -> letter + count.toString() }
            .joinToString(", ")
            .ifEmpty { "(no magimins?)" }

}

data class PotionSpec(
    val tier: PotionTier,
    val type: PotionType,
)

sealed class PotionType(val name: String, val magiminRatio: Magimins) {
    object HealthPotion : PotionType("Health Potion", Magimins(a = 1, b = 1))
    object ManaPotion : PotionType("Mana Potion", Magimins(b = 1, c = 1))
    object StaminaPotion : PotionType("Stamina Potion", Magimins(a = 1, e = 1))
    object SpeedPotion : PotionType("Speed Potion", Magimins(c = 1, d = 1))
    object TolerancePotion : PotionType("Tolerance Potion", Magimins(d = 1, e = 1))

    object FireTonic : PotionType("Fire Tonic", Magimins(a = 1, c = 1))
    object IceTonic : PotionType("Ice Tonic", Magimins(a = 1, d = 1))
    object ThunderTonic : PotionType("Thunder Tonic", Magimins(b = 1, d = 1))
    object ShadowTonic : PotionType("Shadow Tonic", Magimins(b = 1, e = 1))
    object RadiationTonic : PotionType("Radiation Tonic", Magimins(c = 1, e = 1))

    object SightEnhancer : PotionType("Sight Enhancer", Magimins(a = 3, b = 4, c = 3))
    object AlertnessEnhancer : PotionType("Alertness Enhancer", Magimins(b = 3, c = 4, d = 3))
    object InsightEnhancer : PotionType("Insight Enhancer", Magimins(a = 4, b = 3, e = 3))
    object DowsingEnhancer : PotionType("Dowsing Enhancer", Magimins(a = 3, d = 3, e = 4))
    object SeekingEnhancer : PotionType("Seeking Enhancer", Magimins(c = 3, d = 4, e = 3))

    object PoisonCure : PotionType("Poison Cure", Magimins(a = 2, c = 1, d = 1))
    object DrowsinessCure : PotionType("Drowsiness Cure", Magimins(a = 1, b = 1, d = 2))
    object PetrificationCure : PotionType("Petrification Cure", Magimins(a = 1, c = 2, e = 1))
    object SilenceCure : PotionType("Silence Cure", Magimins(b = 2, c = 1, e = 1))
    object CurseCure : PotionType("Curse Cure", Magimins(b = 1, c = 1, e = 2))
}

sealed class PotionTier(val name: String) {
    object Minor : PotionTier("Minor")
    object Common : PotionTier("Common")
    object Greater : PotionTier("Greater")
    object Grand : PotionTier("Grand")
    object Superior : PotionTier("Superior")
    object Masterwork : PotionTier("Masterwork")
}
