package game.data

import godot.core.Color


class BrewingResult(cauldron: CauldronState) {
    val cauldronMagimins = cauldron.ingredients.listItems().fold(magiminCounts())
    { total, ingredient ->
        total.addMagimins(ingredient.magimins)
    }

    val magiminTotal = cauldronMagimins.sum()

    private val potionKind = Potion.Kind.entries.minBy {
        it.magimins.ratioDeviationTo(cauldronMagimins)
    }

    private val potionTier = Potion.Tier.entries.last { tier ->
        magiminTotal >= tier.starRatingMagiminCounts.first()
    }

    private val potionStarRating = potionTier.starRatingMagiminCounts.indexOfLast { count ->
        magiminTotal >= count
    }

    val count = cauldron.ingredients.size / 2

    val deviation = potionKind.magimins.ratioDeviationTo(cauldronMagimins)

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

    // TODO(BUG): need to increase the tier and loop the star rating when adding bonus stars
    val potion = Potion(potionTier, potionKind, potionStarRating + stability.guaranteedBonusStars)

    data class Stability(
        val summary: String,
        val details: String,
        val brewable: Boolean = true,
        val guaranteedBonusStars: Int = 0,
        val chanceBonusStars: Int = 0,
        val color: Color,
    )
}
