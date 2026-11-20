package game

import game.extensions.addCount
import game.extensions.getInstancePlaceholder
import godot.annotation.Script
import godot.api.Control
import godot.extension.getNodeAs


@Script
class BrewingScreen : Control() {
    private val ingredientItemTilePlaceholder by lazy { getInstancePlaceholder<ItemTile>("%IngredientItemTile") }
    private val ingredientList by lazy { getNodeAs<Control>("%IngredientList")!! }


    override fun _ready() {
        val minorHealthPotion = PotionSpec(PotionTier.Minor, PotionType.HealthPotion)
        val minorManaPotion = PotionSpec(PotionTier.Minor, PotionType.ManaPotion)

        val gameData = GameData().run {
            ingredients.addCount(IngredientSpec.Feyberry, 2)
            ingredients.addCount(IngredientSpec.MandrakeRoot, 2)
            ingredients.addCount(IngredientSpec.SackOfSlime, 2)

            potions.addCount(minorHealthPotion, 2)
            potions.addCount(minorManaPotion, 2)

            for ((ingredient, count) in ingredients) {
                val item = ingredientItemTilePlaceholder.createInstance()
                item.itemName = "${ingredient.name}\n${ingredient.magimins.displayText}"
                item.count = count
            }
        }

    }
}
