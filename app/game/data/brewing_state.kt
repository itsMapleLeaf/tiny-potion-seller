package game.data

import godot.core.Color

sealed class BrewingState {
    companion object {
        fun withMagimins(magimins: MagiminCounts) = when {
            magimins.isZero() -> None
            else -> BrewedPotion(magimins)
        }
    }

    object None : BrewingState()

    class BrewedPotion(val magimins: MagiminCounts) : BrewingState() {
        val magiminTotal = magimins.sum()

        val potionKind = Potion.Kind.entries.minBy {
            it.magimins.asRatio().deviationTo(magimins.asRatio())
        }

        // TODO: get the actual star rating within the tier
        val potionTier = Potion.Tier.entries.last { tier ->
            tier.starRatingMagiminCounts.asReversed().any { requiredCount ->
                requiredCount <= magiminTotal
            }
        }

        val potion = Potion(potionTier, potionKind)

        // TODO: get the number of brewed potions based on magimin total
        val count = 1

        val deviation = potion.magimins.ratioDeviationTo(magimins)

        val stability = when {
            deviation <= 0.05 -> {
                Stability(
                    summary = "Perfect!",
                    details = "Potion gains 2 additional stars on completion, with a chance of gaining a bonus star.",
                    guaranteedBonusStars = 2,
                    chanceBonusStars = 1,
                    color = Color("#9adeec"),
                )
            }

            deviation <= 0.1 -> {
                Stability(
                    summary = "Very stable!",
                    details = "Potion gains 1 additional star on completion, with a chance of gaining a bonus star.",
                    guaranteedBonusStars = 1,
                    chanceBonusStars = 1,
                    color = Color("#a1ec9a"),
                )
            }

            deviation <= 0.3 -> {
                Stability(
                    summary = "Stable",
                    details = "Chance of potion gaining 1 additional star on completion.",
                    guaranteedBonusStars = 0,
                    chanceBonusStars = 1,
                    color = Color("#e4ec9a"),
                )
            }

            deviation <= 0.5 -> {
                Stability(
                    summary = "Unstable",
                    details = "Chance of potion losing a star on completion.",
                    guaranteedBonusStars = 0,
                    chanceBonusStars = -1,
                    color = Color("#eccf9a"),
                )
            }

            else -> {
                Stability(
                    summary = "Too unstable",
                    details = "Can't brew.",
                    brewable = false,
                    color = Color("#ec9a9a"),
                )
            }
        }
    }

    data class Stability(
        val summary: String,
        val details: String,
        val brewable: Boolean = true,
        val guaranteedBonusStars: Int = 0,
        val chanceBonusStars: Int = 0,
        val color: Color,
    )
}
