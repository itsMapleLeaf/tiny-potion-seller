package game.components

import game.data.GameData
import game.data.IngredientSpec
import game.data.Magimin
import game.data.MagiminCounts
import game.data.Potion
import game.data.addMagimins
import game.data.asRatio
import game.data.deviationTo
import game.data.displayText
import game.data.magiminCounts
import game.data.multiplyMagimins
import game.extensions.addCount
import game.extensions.counterMapOf
import game.extensions.lazyNode
import game.extensions.removeCount
import godot.annotation.Script
import godot.api.Button
import godot.api.Control
import godot.api.InstancePlaceholder
import godot.api.Node
import godot.api.PackedScene
import godot.common.util.clampValue
import godot.common.util.lerp
import godot.extension.connectLambda
import godot.extension.getNodeAs
import godot.global.GD

@Script
class BrewingPanel : Control() {
    var gameData = GameData()

    private val itemTileScene = GD.load<PackedScene>("res://game/components/item_tile.tscn")!!

    private val ingredientsInCauldron = counterMapOf<IngredientSpec>()

    private val ingredientList by lazyNode<Control>("%IngredientList")
    private val cauldronIngredientList by lazyNode<Control>("%CauldronIngredientList")
    private val potionList by lazyNode<Control>("%PotionList")
    private val brewButton by lazyNode<Button>("%BrewButton")
    private val brewingResultPanel by lazyNode<BrewingResultPanel>("%BrewingResultPanel")

    private val cauldronMagimins
        get(): MagiminCounts = ingredientsInCauldron
            .map { (i, count) -> i.magimins.multiplyMagimins(count) }
            .fold(magiminCounts()) { a, b -> a.addMagimins(b) }

    private val cauldronMagiminTotal get() = cauldronMagimins.sum()
    private val cauldronMagiminRatio get() = cauldronMagimins.asRatio()

    private fun getBrewingResult(): BrewingResult? {
        if (cauldronMagiminTotal == 0) return null

        val kind = Potion.Kind.entries.minBy {
            it.magimins.asRatio().deviationTo(cauldronMagiminRatio)
        }

        // TODO: get the actual star rating within the tier
        val tier = Potion.Tier.entries.last { tier ->
            tier.starRatingMagiminCounts.asReversed().any { requiredCount ->
                requiredCount <= cauldronMagiminTotal
            }
        }

        // TODO: figure out how many potions we got based on how many magis we put in
        val count = 1

        return BrewingResult(cauldronMagimins, Potion(tier, kind), count)
    }


    override fun _ready() {
        brewButton.pressed.connectLambda {
            val result = getBrewingResult() ?: return@connectLambda

            gameData.potions.addCount(result.potion, result.count)
            ingredientsInCauldron.clear()
            refresh()
        }

        refresh()
    }

    fun refresh() {
        populateItemCollection(
            gameData.ingredients.toSortedMap { a, b ->
                a.name.compareTo(b.name)
            }.entries,
            ingredientList,
        ) { (ingredient, count), tile ->
            tile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            tile.count = count
            tile.pressed.connectLambda {
                gameData.ingredients.removeCount(ingredient)
                ingredientsInCauldron.addCount(ingredient)
                refresh()
            }
        }

        populateItemCollection(
            ingredientsInCauldron.toSortedMap { a, b ->
                a.name.compareTo(b.name)
            }.entries,
            cauldronIngredientList,
        ) { (ingredient, count), tile ->
            tile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            tile.count = count
            tile.pressed.connectLambda {
                ingredientsInCauldron.removeCount(ingredient)
                gameData.ingredients.addCount(ingredient)
                refresh()
            }
        }

        populateItemCollection(
            gameData.potions.toSortedMap { a, b ->
                a.displayName.compareTo(b.displayName)
            }.entries,
            potionList,
        ) { (potion, count), tile ->
            tile.itemName = potion.displayName
            tile.count = count
        }

        brewingResultPanel.result = getBrewingResult()
    }

    private fun <T> populateItemCollection(
        dataItems: Iterable<T>,
        container: Node,
        configureItemTile: (item: T, tile: ItemTile) -> Unit,
    ) {
        for (node in container.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for (item in dataItems) {
            val itemTile = itemTileScene.instantiate() as ItemTile
            configureItemTile(item, itemTile)
            container.addChild(itemTile)
        }
    }
}
