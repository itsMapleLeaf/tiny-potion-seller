package game.data


data class GameData(
    var dayNumber: Int = 1,
    var timeRemaining: Int = 6,
    var gold: Int = 0,
    val ingredients: MutableMap<IngredientSpec, Int> = mutableMapOf(),
    val potions: MutableMap<PotionData, Int> = mutableMapOf(),
)

data class PotionData(
    val tier: PotionTier,
    val type: PotionSpec,
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

data class PotionSpec(val name: String, val magiminRatio: MagiminRatio) {
    init {
        libraryInternal.add(this)
    }

    companion object {
        val library get(): List<PotionSpec> = libraryInternal

        private val libraryInternal = mutableListOf<PotionSpec>()

        val HealthPotion = PotionSpec("Health Potion", magiminRatio(a = 1, b = 1))
        val ManaPotion = PotionSpec("Mana Potion", magiminRatio(b = 1, c = 1))
        val StaminaPotion = PotionSpec("Stamina Potion", magiminRatio(a = 1, e = 1))
        val SpeedPotion = PotionSpec("Speed Potion", magiminRatio(c = 1, d = 1))
        val TolerancePotion = PotionSpec("Tolerance Potion", magiminRatio(d = 1, e = 1))

        val FireTonic = PotionSpec("Fire Tonic", magiminRatio(a = 1, c = 1))
        val IceTonic = PotionSpec("Ice Tonic", magiminRatio(a = 1, d = 1))
        val ThunderTonic = PotionSpec("Thunder Tonic", magiminRatio(b = 1, d = 1))
        val ShadowTonic = PotionSpec("Shadow Tonic", magiminRatio(b = 1, e = 1))
        val RadiationTonic = PotionSpec("Radiation Tonic", magiminRatio(c = 1, e = 1))

        val SightEnhancer = PotionSpec("Sight Enhancer", magiminRatio(a = 3, b = 4, c = 3))
        val AlertnessEnhancer = PotionSpec("Alertness Enhancer", magiminRatio(b = 3, c = 4, d = 3))
        val InsightEnhancer = PotionSpec("Insight Enhancer", magiminRatio(a = 4, b = 3, e = 3))
        val DowsingEnhancer = PotionSpec("Dowsing Enhancer", magiminRatio(a = 3, d = 3, e = 4))
        val SeekingEnhancer = PotionSpec("Seeking Enhancer", magiminRatio(c = 3, d = 4, e = 3))

        val PoisonCure = PotionSpec("Poison Cure", magiminRatio(a = 2, c = 1, d = 1))
        val DrowsinessCure = PotionSpec("Drowsiness Cure", magiminRatio(a = 1, b = 1, d = 2))
        val PetrificationCure = PotionSpec("Petrification Cure", magiminRatio(a = 1, c = 2, e = 1))
        val SilenceCure = PotionSpec("Silence Cure", magiminRatio(b = 2, c = 1, e = 1))
        val CurseCure = PotionSpec("Curse Cure", magiminRatio(b = 1, c = 1, e = 2))
    }
}

data class PotionTier(val name: String, var starRatingMagiminCounts: List<Int>) {
    init {
        libraryInternal.add(this)
    }

    companion object {
        val library get(): List<PotionTier> = libraryInternal

        private val libraryInternal = mutableListOf<PotionTier>()

        val Minor = PotionTier("Minor", listOf(0, 10, 20, 30, 40, 50))
        val Common = PotionTier("Common", listOf(60, 75, 90, 105, 115, 130))
        val Greater = PotionTier("Greater", listOf(150, 170, 195, 215, 235, 260))
        val Grand = PotionTier("Grand", listOf(290, 315, 345, 370, 400, 430))
        val Superior = PotionTier("Superior", listOf(470, 505, 545, 580, 620, 660))
        val Masterwork = PotionTier("Masterwork", listOf(720, 800, 875, 960, 1040, 1121))
    }
}
