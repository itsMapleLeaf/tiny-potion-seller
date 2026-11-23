package game.data

import godot.annotation.Export
import godot.annotation.Script
import godot.api.Resource
import godot.common.extensions.convertToTitleCase

@Script
class Potion(
    @Export val tier: Tier,
    @Export val kind: Kind,
) : Resource() {
    val displayName get() = "${tier.displayName} ${kind.displayName}"
    val magimins get() = kind.magimins

    enum class Kind(val magimins: MagiminCounts) {
        HealthPotion(magiminCounts(a = 1, b = 1)),
        ManaPotion(magiminCounts(b = 1, c = 1)),
        StaminaPotion(magiminCounts(a = 1, e = 1)),
        SpeedPotion(magiminCounts(c = 1, d = 1)),
        TolerancePotion(magiminCounts(d = 1, e = 1)),

        FireTonic(magiminCounts(a = 1, c = 1)),
        IceTonic(magiminCounts(a = 1, d = 1)),
        ThunderTonic(magiminCounts(b = 1, d = 1)),
        ShadowTonic(magiminCounts(b = 1, e = 1)),
        RadiationTonic(magiminCounts(c = 1, e = 1)),

        SightEnhancer(magiminCounts(a = 3, b = 4, c = 3)),
        AlertnessEnhancer(magiminCounts(b = 3, c = 4, d = 3)),
        InsightEnhancer(magiminCounts(a = 4, b = 3, e = 3)),
        DowsingEnhancer(magiminCounts(a = 3, d = 3, e = 4)),
        SeekingEnhancer(magiminCounts(c = 3, d = 4, e = 3)),

        PoisonCure(magiminCounts(a = 2, c = 1, d = 1)),
        DrowsinessCure(magiminCounts(a = 1, b = 1, d = 2)),
        PetrificationCure(magiminCounts(a = 1, c = 2, e = 1)),
        SilenceCure(magiminCounts(b = 2, c = 1, e = 1)),
        CurseCure(magiminCounts(b = 1, c = 1, e = 2));

        val displayName = name.convertToTitleCase()
    }

    enum class Tier(val starRatingMagiminCounts: List<Int>) {
        Minor(listOf(0, 10, 20, 30, 40, 50)),
        Common(listOf(60, 75, 90, 105, 115, 130)),
        Greater(listOf(150, 170, 195, 215, 235, 260)),
        Grand(listOf(290, 315, 345, 370, 400, 430)),
        Superior(listOf(470, 505, 545, 580, 620, 660)),
        Masterwork(listOf(720, 800, 875, 960, 1040, 1121));

        val displayName = name
    }
}
