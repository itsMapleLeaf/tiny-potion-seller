package game

import game.extensions.addCount
import game.extensions.counterMapOf
import game.extensions.getInstancePlaceholder
import game.extensions.removeCount
import godot.annotation.Script
import godot.api.Control
import godot.api.InstancePlaceholder
import godot.extension.connectLambda
import godot.extension.getNodeAs


@Script
class BrewingScreen : Control() {
    private val ingredientsInCauldron = counterMapOf<IngredientSpec>()

    private val ingredientList by lazy { getNodeAs<Control>("%IngredientList")!! }
    private val ingredientItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%IngredientItemTile") }
    private val cauldronIngredientList by lazy { getNodeAs<Control>("%CauldronIngredientList")!! }
    private val cauldronIngredientItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%CauldronIngredientItemTile") }


    val gameData = GameData().apply {
        ingredients.addCount(IngredientSpec.Feyberry, 2)
        ingredients.addCount(IngredientSpec.MandrakeRoot, 2)
        ingredients.addCount(IngredientSpec.SackOfSlime, 2)

        val minorHealthPotion = PotionSpec(PotionTier.Minor, PotionType.HealthPotion)
        val minorManaPotion = PotionSpec(PotionTier.Minor, PotionType.ManaPotion)

        potions.addCount(minorHealthPotion, 2)
        potions.addCount(minorManaPotion, 2)
    }

    override fun _ready() {
        buildUi()
    }

    private fun buildUi() {
        for (node in ingredientList.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        var compareIngredientNames: Comparator<IngredientSpec> = { a, b ->
            a.name.compareTo(b.name)
        }

        for ((ingredient, count) in gameData.ingredients.toSortedMap(compareIngredientNames)) {
            val itemTile = ingredientItemTilePlaceholder.createInstance()
            itemTile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            itemTile.count = count
            itemTile.pressed.connectLambda {
                gameData.ingredients.removeCount(ingredient)
                ingredientsInCauldron.addCount(ingredient)
                buildUi()
            }
        }

        for (node in cauldronIngredientList.getChildren()) {
            if (node !is InstancePlaceholder) {
                node.queueFree()
            }
        }

        for ((ingredient, count) in ingredientsInCauldron.toSortedMap(compareIngredientNames)) {
            val itemTile = cauldronIngredientItemTilePlaceholder.createInstance()
            itemTile.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
            itemTile.count = count
            itemTile.pressed.connectLambda {
                ingredientsInCauldron.removeCount(ingredient)
                gameData.ingredients.addCount(ingredient)
                buildUi()
            }
        }
    }
}
