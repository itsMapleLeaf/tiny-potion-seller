package game

import game.data.GameData
import game.data.HasName
import game.data.IngredientSpec
import game.data.MagiminCounts
import game.data.PotionData
import game.data.PotionTier
import game.data.PotionSpec
import game.data.asRatio
import game.data.deviationTo
import game.data.displayText
import game.data.magiminCounts
import game.extensions.CounterMap
import game.extensions.addCount
import game.extensions.counterMapOf
import game.extensions.getInstancePlaceholder
import game.extensions.removeCount
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.InstancePlaceholder
import godot.api.Label
import godot.api.Node
import godot.extension.connectLambda
import godot.extension.getNodeAs
import godot.global.GD
import kotlin.math.abs


@Script
class BrewingScreen : Control() {
    private val ingredientsInCauldron = counterMapOf<IngredientSpec>()

    private val ingredientList by lazy { getNodeAs<Control>("%IngredientList")!! }
    private val ingredientItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%IngredientItemTile") }

    private val cauldronIngredientList by lazy { getNodeAs<Control>("%CauldronIngredientList")!! }
    private val cauldronIngredientItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%CauldronIngredientItemTile") }

    private val potionList by lazy { getNodeAs<Control>("%PotionList")!! }
    private val potionItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%PotionItemTile") }

    private val outputLabel by lazy { getNodeAs<Label>("%OutputLabel")!! }
    private val brewButton by lazy { getNodeAs<Button>("%BrewButton")!! }


    val gameData = GameData().apply {
        ingredients.addCount(IngredientSpec.Feyberry, 10)
        ingredients.addCount(IngredientSpec.MandrakeRoot, 10)
        ingredients.addCount(IngredientSpec.SackOfSlime, 10)

        val minorHealthPotion = PotionData(PotionTier.Minor, PotionSpec.HealthPotion)
        val minorManaPotion = PotionData(PotionTier.Minor, PotionSpec.ManaPotion)

        potions.addCount(minorHealthPotion, 2)
        potions.addCount(minorManaPotion, 2)
    }

    val cauldronMagimins
        get(): MagiminCounts = ingredientsInCauldron
            .map { (i, count) -> i.magimins.map { it * count } }
            .reduce { a, b ->
                a.zip(b).map { (a, b) -> a + b }
            }

    val cauldronMagiminRatio get() = cauldronMagimins.asRatio()

    val deviations
        get() = PotionSpec.library.map {
            it to it.magiminRatio.deviationTo(cauldronMagiminRatio)
        }


    override fun _ready() {
        brewButton.pressed.connectLambda {
            val (matchingPotion, deviation) = deviations.minBy { (_, d) -> d }
            val totalMagimins = cauldronMagimins.sum()

            // TODO: get the actual star rating within the tier
            val tier = PotionTier.library.last { tier ->
                tier.starRatingMagiminCounts.asReversed().any { requiredCount ->
                    requiredCount <= totalMagimins
                }
            }

            // TODO: figure out how many potions we got based on how many magis we put in
            val brewedCount = 1

            gameData.potions.addCount(PotionData(tier, matchingPotion), brewedCount)
            ingredientsInCauldron.clear()
            refresh()

            GD.print(gameData.potions)
        }

        refresh()
    }

    private fun refresh() {
        val compareNames: Comparator<in HasName> =
            { a, b -> a.name.compareTo(b.name) }

        for (node in ingredientList.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for ((ingredient, count) in gameData.ingredients.toSortedMap(compareNames)) {
            val itemTile = ingredientItemTilePlaceholder.createInstance()
            itemTile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            itemTile.count = count
            itemTile.pressed.connectLambda {
                gameData.ingredients.removeCount(ingredient)
                ingredientsInCauldron.addCount(ingredient)
                refresh()
            }
        }

        for (node in cauldronIngredientList.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for ((ingredient, count) in ingredientsInCauldron.toSortedMap(compareNames)) {
            val itemTile = cauldronIngredientItemTilePlaceholder.createInstance()
            itemTile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            itemTile.count = count
            itemTile.pressed.connectLambda {
                ingredientsInCauldron.removeCount(ingredient)
                gameData.ingredients.addCount(ingredient)
                refresh()
            }
        }

        for (node in potionList.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for ((potion, count) in gameData.potions.toSortedMap(compareNames)) {
            val itemTile = potionItemTilePlaceholder.createInstance()
            itemTile.itemName = potion.name
            itemTile.count = count
            // itemTile.pressed.connectLambda {
            //     ingredientsInCauldron.removeCount(potion)
            //     gameData.ingredients.addCount(potion)
            //     refresh()
            // }
        }

        if (ingredientsInCauldron.isEmpty()) {
            outputLabel.text = ""
        } else {
            val (matchingPotion, deviation) = deviations.minBy { (_, d) -> d }

            val deviationListText = deviations.joinToString("\n") { (pot, dev) ->
                "${pot.name}: $dev (${pot.magiminRatio})"
            }

            outputLabel.text = listOf(
                "Magimins: ${cauldronMagimins.displayText}",
                "Ratio: $cauldronMagiminRatio",
                "Potion: ${matchingPotion.name}",
                "Deviation: $deviation",
                "---",
                deviationListText
            ).joinToString("\n")
        }
    }
}
